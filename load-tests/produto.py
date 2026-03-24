import os
import random
from locust import HttpUser, task, between
from faker import Faker
from dotenv import load_dotenv

load_dotenv()

fake = Faker('pt_BR')

class ProdutoUser(HttpUser):
    token_global = None
    wait_time = between(1, 2)

    def on_start(self):
        if not ProdutoUser.token_global:
            ProdutoUser.token_global = self.obter_token()

        self.headers = {
            "Authorization": f"Bearer {ProdutoUser.token_global}",
            "Content-Type": "application/json"
        }

    def obter_token(self):
        payload = {
            "grant_type": "client_credentials",
            "client_id": os.getenv("CLIENT_ID"),
            "client_secret": os.getenv("CLIENT_SECRET"),
            "audience": os.getenv("API_AUDIENCE")
        }

        response = self.client.post(
            os.getenv("AUTH_URL"),
            json=payload,
            name="POST /oauth/token"
        )

        if response.status_code != 200:
            raise Exception(f"Erro ao gerar token: {response.status_code} | {response.text}")

        body = response.json()
        token = body.get("access_token")

        if not token:
            raise Exception(f"Token não encontrado. Resposta: {body}")

        return token

    @task
    def fluxo_criar_buscar_produto(self):
        tipos_produto = ["Pomada", "Shampoo", "Óleo", "Cera", "Pente", "Loção", "Bálsamo"]

        payload = {
            "nome": f"{random.choice(tipos_produto)} {fake.word().capitalize()} {fake.random_int(min=100, max=9999)}",
            "precoVenda": round(fake.pyfloat(left_digits=3, right_digits=2, positive=True, min_value=10.0, max_value=200.0), 2),
            "quantidadeEstoque": fake.random_int(min=10, max=100),
            "estoqueMinimo": fake.random_int(min=1, max=5),
            "ativo": True,
            "descricao": fake.sentence()
        }

        with self.client.post(
            "/produtos",
            json=payload,
            headers = self.headers,
            name="POST /produto",
            catch_response=True
        ) as response_post:

            if response_post.status_code not in [200, 201]:
                response_post.failure(f"Falha na criação: {response_post.status_code} | {response_post.text}")
                return

            try:
                corpo_resposta = response_post.json()
            except Exception:
                response_post.failure(f"A API não retornou um JSON válido. Retornou: {response_post.text}")
                return

        produto_id = None

        if isinstance(corpo_resposta, dict) and isinstance(corpo_resposta.get("dados"), dict):
            produto_id = corpo_resposta["dados"].get("id")

        if not produto_id:
            print(f"ALERTA: ID não encontrado na resposta. Resposta: {corpo_resposta}")
            return
        with self.client.get(
            f"/produtos/{produto_id}",
            headers = self.headers,
            name="GET /produto/{id}",
            catch_response=True
        ) as response_get:

            if response_get.status_code != 200:
                response_get.failure(f"Falha na busca: {response_get.status_code} | {response_get.text}")