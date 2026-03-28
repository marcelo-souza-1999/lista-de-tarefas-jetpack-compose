````markdown
# 📝 Lista de Tarefas - Jetpack Compose

Um aplicativo seguindo as boas práticas de desenvolvimento Android, focado em produtividade,
desenvolvido nativamente utilizando **Kotlin** e **Jetpack Compose**.

O objetivo principal deste projeto é demonstrar a aplicação de engenharia de software, arquitetura
limpa e testes automatizados no ecossistema Android moderno. O app permite aos usuários criar
contas, autenticar-se e gerenciar tarefas de forma intuitiva, com tratamento completo de erros e
feedback visual através de dialogs customizados.

---

## 🚀 Tecnologias e Ferramentas

Este projeto utiliza o que há de mais moderno no desenvolvimento Android:

* **UI:** Jetpack Compose e Material Design 3
* **Linguagem:** Kotlin
* **Arquitetura:** Clean Architecture com MVVM / MVI
* **Injeção de Dependência:** Koin Annotations
* **Navegação:** Jetpack Navigation 3 Compose (Type-Safe)
* **Testes:** JUnit, MockK, Turbine, Kover (Cobertura), Paparazzi (Snapshot) e Testes Instrumentados
* **CI/CD:** GitHub Actions com DangerJS para Code Review automatizado

---

## 📁 Estrutura do Projeto

O projeto segue os princípios da **Clean Architecture**, dividindo responsabilidades de forma clara
e escalável:

* 📦 **data**: Camada de dados responsável por obter e fornecer informações.
    * `datasource`: Implementações de acesso a dados (APIs, Banco de Dados).
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

* **Tratamento de Erros e Feedback:** Uso de `Dialogs` customizados (`ErrorDialog`, `SuccessDialog`,
  `DeleteDialog`) para garantir feedback em todas as operações.
* **Gerenciamento de Estado:** Uso de classes seladas (`UiState`) para representar estados de
  *Loading*, *Success* e *Error*.
* **Navegação Desacoplada:** Gerenciamento via `AppNavigator`, evitando acoplamento entre a UI e o
  `NavController`.
* **Código Limpo:** Uso de extensões utilitárias e mapeamento de erros para manter as Views focadas
  apenas em renderização.

---

## 🧪 Testes e Cobertura (Kover)

A qualidade é garantida por uma suíte de testes robusta. Utilizamos o **Kover** para gerar
relatórios de cobertura, garantindo a proteção das regras de negócio.

### 📸 Testes de Snapshot (Screenshot Tests)

Para evitar regressões visuais, utilizamos testes de snapshot.

* **Dispositivo Base:** Imagens geradas simulando um **Motorola Edge 50 Neo** (6.4", 1220 x 2670 px,
  Super HD).
* **Atenção:** Emuladores com densidades de pixels diferentes podem causar falhas na validação.

**Como regravar as imagens de referência:**

```bash
./gradlew recordPaparazziDebug
````

### 🛠️ Script de Testes Facilitado

Para rodar todos os testes (Unitários, Instrumentados e Lint) de uma só vez:

```bash
./run_tests.sh
```

-----

## ⚙️ CI/CD e Esteira Automatizada

Configurada via **GitHub Actions** (`android.yml`). Em cada Pull Request:

1.  Compilação do código e execução de testes unitários.
2.  Análise via **DangerJS**, que comenta automaticamente no PR sobre qualidade e cobertura.

-----

## ▶️ Como Executar o Projeto

1.  Clone o repositório:
    ```bash
    git clone [https://github.com/marcelo-souza-1999/lista-de-tarefas-jetpack-compose.git](https://github.com/marcelo-souza-1999/lista-de-tarefas-jetpack-compose.git)
    ```
2.  Abra no **Android Studio** (Ladybug ou superior).
3.  Execute o app em um emulador ou dispositivo físico (`Shift + F10`).

-----

## 🤝 Como Contribuir

1.  Faça um **Fork** do projeto.
2.  Crie uma **Branch** (`git checkout -b feature/minha-feature`).
3.  Comite suas mudanças e execute os testes (`./run_tests.sh`).
4.  Abra um **Pull Request**.

-----

## 🎥 Demonstração do App

*(Para adicionar o vídeo, arraste o arquivo .mp4 para esta seção durante a edição no GitHub)*

-----

## 📫 Contato

Desenvolvido com ☕ e 💻 por **Marcelo Souza**:

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/marcelosouza-1999/)
[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:marcelocaregnatodesouza@gmail.com)

```