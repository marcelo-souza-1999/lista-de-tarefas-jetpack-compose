# 📝 Lista de Tarefas - Jetpack Compose

Um aplicativo seguindo as boas práticas de desenvolvimento Android, focado em produtividade, desenvolvido nativamente utilizando **Kotlin** e **Jetpack Compose**.

O objetivo principal deste projeto é demonstrar a aplicação de engenharia de software, arquitetura limpa e testes automatizados no ecossistema Android moderno. O app permite aos usuários criar contas, autenticar-se e gerenciar tarefas de forma intuitiva, com tratamento completo de erros e feedback visual através de dialogs customizados.

---

## 🚀 Tecnologias e Ferramentas

Este projeto utiliza o que há de mais moderno no desenvolvimento Android:

* **UI:** Jetpack Compose e Material Design 3
* **Linguagem:** Kotlin
* **Backend/Auth:** Firebase (Authentication e Firestore)
* **Arquitetura:** Clean Architecture com MVVM / MVI
* **Injeção de Dependência:** Koin Annotations
* **Navegação:** Jetpack Navigation 3 Compose (Type-Safe)
* **Testes:** JUnit, MockK, Turbine, Kover (Cobertura), Paparazzi (Snapshot) e Testes Instrumentados
* **CI/CD:** GitHub Actions com DangerJS para Code Review automatizado

---

## 📁 Estrutura do Projeto

O projeto segue os princípios da **Clean Architecture**, dividindo responsabilidades de forma clara e escalável:

* 📦 **data**: Camada de dados responsável por obter e fornecer informações.
    * `datasource`: Implementações de acesso a dados (Firebase Firestore, Auth).
    * `mapper`: Conversores entre modelos de dados (DTOs) e modelos de domínio.
    * `model`: Modelos de transferência de dados (Ex: `TaskDto`).
    * `repository`: Implementação concreta das interfaces do repositório.
* 📦 **di**: Configuração de Injeção de Dependência com Koin (`AppModule`, `DataSourceModule`).
* 📦 **domain**: Camada de regras de negócio pura, sem dependências do Android.
    * `model`: Entidades de negócio (Ex: `Task`, `TaskPriority`).
    * `repository`: Interfaces que definem os contratos de dados.
* 📦 **presentation**: Camada de UI e gerenciamento de estado visual.
    * `navigation`: Gerenciador centralizado de rotas e eventos de navegação.
    * `theme`: Sistema de design (Cores, Tipografia, Dimensões).
    * `ui`: Telas construídas 100% em Compose (`home`, `authenticate`, `splash`, `task`).
    * `components`: Componentes reutilizáveis como `Buttons`, `CardView` e `Dialogs`.
    * `viewmodel`: Gerenciadores de estado da UI e regras de apresentação.

---

## ✨ Boas Práticas e Funcionalidades

* **Integração com Firebase:** Autenticação segura e armazenamento de tarefas em tempo real.
* **Tratamento de Erros e Feedback:** Uso de `Dialogs` customizados para garantir feedback em todas as operações (Erro, Sucesso, Confirmação).
* **Gerenciamento de Estado:** Uso de classes seladas (`UiState`) para representar estados de *Loading*, *Success* e *Error*.
* **Navegação Desacoplada:** Gerenciamento via `AppNavigator`, evitando acoplamento da UI com o `NavController`.

---

## 🧪 Testes e Cobertura (Kover)

A qualidade é garantida por uma suíte de testes robusta. Utilizamos o **Kover** para gerar relatórios de cobertura das regras de negócio.

### 📸 Testes de Snapshot (Screenshot Tests)

Para evitar regressões visuais, utilizamos testes de snapshot.
* **Dispositivo Base:** Motorola Edge 50 Neo (6.4", 1220 x 2670 px).
* **Comando para gravar snapshots:** `./gradlew recordPaparazziDebug`

### 🛠️ Script de Testes Facilitado

Para rodar todos os testes (Unitários, Instrumentados e Lint) de uma só vez:
```bash
./run_tests.sh
````

-----

## ⚙️ CI/CD e Esteira Automatizada

Configurada via **GitHub Actions** (`android.yml`). Em cada Pull Request:

1.  Compilação e execução de testes unitários.
2.  Análise via **DangerJS**, que comenta no PR sobre qualidade e cobertura.

-----

## ▶️ Como Executar o Projeto

### 1\. Configuração do Firebase (Obrigatório)

Este projeto utiliza o Firebase. Antes de rodar, você precisa:

1.  Criar um projeto no [Firebase Console](https://console.firebase.google.com/).
2.  Ativar **Authentication** (E-mail/Senha) e **Cloud Firestore**.
3.  Adicionar um app Android ao projeto com o package name `com.marcelo.souza.api.filmes` (ou o configurado no seu `build.gradle`).
4.  Baixar o arquivo `google-services.json` e colá-lo dentro da pasta `app/` do projeto.

### 2\. Rodar o App

1.  Clone o repositório:
    ```bash
    git clone https://github.com/marcelo-souza-1999/lista-de-tarefas-jetpack-compose.git
    ```
2.  Abra no **Android Studio** (Ladybug ou superior).
3.  Execute em um emulador ou dispositivo físico (`Shift + F10`).

-----

## 🤝 Como Contribuir

1.  Faça um **Fork** do projeto.
2.  Crie uma **Branch** (`git checkout -b feature/minha-feature`).
3.  Comite suas mudanças e execute os testes (`./run_tests.sh`).
4.  Abra um **Pull Request**.

-----

## 🎥 Demonstração do App

https://github.com/user-attachments/assets/16f2c81c-f7d4-4e69-9dd4-dbe647156164

-----

## 📫 Contato

Desenvolvido com ☕ e 💻 por **Marcelo Souza**:

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/marcelosouza-1999/)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:marcelocaregnatodesouza@gmail.com)
