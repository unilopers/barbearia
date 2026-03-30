import os
import uuid
import random
from locust import HttpUser, task, between
from dotenv import load_dotenv

load_dotenv()


class FornecedorUser(HttpUser):
    wait_time = between(1, 2)
    token_global = None

    def on_start(self):
        if not FornecedorUser.token_global:
            FornecedorUser.token_global = self.obter_token()

        self.headers = {
            "Authorization": f"Bearer {FornecedorUser.token_global}",
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

    def gerar_cnpj_fake(self):
        # Gera 14 dígitos numéricos
        return ''.join(random.choices("0123456789", k=14))

    @task
    def criar_e_buscar_fornecedor(self):
        identificador = uuid.uuid4().hex[:8]
        cnpj_unico = self.gerar_cnpj_fake()

        fornecedor_payload = {
            "nome": f"Fornecedor Teste {identificador}",
            "cnpj": cnpj_unico,
            "email": f"fornecedor{identificador}@teste.com",
            "telefone": "43999999999"
        }

        with self.client.post(
            "/fornecedores",
            json=fornecedor_payload,
            headers=self.headers,
            name="POST /fornecedores",
            catch_response=True
        ) as response:

            if response.status_code not in [200, 201]:
                response.failure(
                    f"Erro ao criar fornecedor: {response.status_code} | {response.text}"
                )
                return

            try:
                body = response.json()
            except Exception:
                response.failure(f"Resposta do cadastro não é JSON válido: {response.text}")
                return

            print("Resposta cadastro:", body)

            # Ajuste conforme o formato real da sua API
            fornecedor_id = body.get("id") or body.get("dados", {}).get("id")

            if not fornecedor_id:
                response.failure(f"ID do fornecedor não retornado: {body}")
                return

            response.success()

        with self.client.get(
            f"/fornecedores/{fornecedor_id}",
            headers=self.headers,
            name="GET /fornecedores/{id}",
            catch_response=True
        ) as response:

            if response.status_code != 200:
                response.failure(
                    f"Erro ao buscar fornecedor: {response.status_code} | {response.text}"
                )
                return

            try:
                body = response.json()
            except Exception:
                response.failure(f"Resposta da busca não é JSON válido: {response.text}")
                return

            print("Resposta busca:", body)

            fornecedor_buscado = body.get("dados", body)
            id_retorno = fornecedor_buscado.get("id")

            if str(id_retorno) != str(fornecedor_id):
                response.failure(
                    f"Fornecedor retornado não corresponde ao criado. Esperado ID {fornecedor_id}, recebido: {body}"
                )
                return

            response.success()