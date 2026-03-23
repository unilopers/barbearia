import time
import random
import os
from locust import HttpUser, task, between
from dotenv import load_dotenv

load_dotenv()


class ClienteUser(HttpUser):
    wait_time = between(1, 2)
    token_global = None

    def on_start(self):
        if not ClienteUser.token_global:
            ClienteUser.token_global = self.obter_token()

        self.headers = {
            "Authorization": f"Bearer {ClienteUser.token_global}",
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
    def criar_e_buscar_cliente(self):
        timestamp = int(time.time() * 1000)

        payload = {
            "nome": f"Cliente Teste {timestamp}",
            "telefone": f"4399{random.randint(1000000, 9999999)}",
            "email": f"cliente{timestamp}@teste.com"
        }

        with self.client.post(
            "/clientes",
            json=payload,
            headers=self.headers,
            name="POST /clientes",
            catch_response=True
        ) as response:

            if response.status_code not in [200, 201]:
                response.failure(f"POST falhou: {response.status_code} | {response.text}")
                return

            try:
                body = response.json()
            except Exception:
                response.failure(f"POST não retornou JSON. Texto: {response.text}")
                return

        cliente_id = None

        if isinstance(body, dict) and isinstance(body.get("dados"), dict):
            cliente_id = body["dados"].get("id")

        if not cliente_id:
            print("ID NÃO ENCONTRADO:", body)
            return

        with self.client.get(
            f"/clientes/{cliente_id}",
            headers=self.headers,
            name="GET /clientes/{id}",
            catch_response=True
        ) as response:

            if response.status_code != 200:
                response.failure(f"GET falhou: {response.status_code} | {response.text}")