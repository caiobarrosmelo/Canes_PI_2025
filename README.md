# Canes

**Canes** é um aplicativo mobile para **Android**, projetado para facilitar a **gestão de ações estratégicas** dentro de organizações. Com ele, é possível cadastrar pilares, alocar responsabilidades, gerar relatórios e visualizar dados em dashboards interativos — tudo de forma prática e acessível via smartphone.

---

## Tecnologias Utilizadas

- **Kotlin** – Linguagem principal de desenvolvimento
- **Android Studio Meerkat** – Ambiente de desenvolvimento
- **API 31 (Android 12)** – Compatibilidade mínima

---

## Instruções de Instalação e Execução

[Baixar APK]()

1. No seu celular, acesse o link acima e faça o download do arquivo APK.
2. Quando o download for concluído, toque no arquivo APK para iniciar a instalação.
3. Se aparecer um aviso de segurança, permita a instalação de **“Fontes desconhecidas”** ou **“Permitir desta fonte”**, conforme o seu aparelho.
   - Normalmente, essa opção aparece automaticamente quando você tenta instalar.
   - Se não aparecer, vá em:
     - **Configurações → Segurança → Instalar apps de fontes desconhecidas** (ou semelhante, dependendo do aparelho).
4. Conclua a instalação seguindo as instruções na tela.
5. Após instalado, procure o ícone do app na sua lista de aplicativos e abra normalmente.

### Observação
- Esse aplicativo é destinado para fins de teste e desenvolvimento.
- Caso atualize uma nova versão do APK, será necessário desinstalar a versão anterior antes de instalar a nova (salvo se você configurar a mesma chave de assinatura no APK).

---

## Estrutura do Projeto

 O projeto está organizado em pacotes que refletem as responsabilidades de cada componente do sistema, seguindo uma abordagem modular e coesa. A estrutura do projeto é apresentada abaixo:
 
- **`manifests/`**  
  Contém o arquivo `AndroidManifest.xml`, responsável pelas declarações de permissões, atividades e configurações principais do app.

- **`java/com.example.pi3/`**  
  Contém o código-fonte em Kotlin, organizado nos seguintes pacotes:
  - **`adapters/`** – Adapters usados em RecyclerViews para listar ações e atividades.
  - **`apoio/`** –  Telas (Activities) e lógica de navegação para usuários do tipo Apoio.
  - **`coordenador/`** – Telas (Activities) específicas do Coordenador.
  - **`gestor/`** – Interface de visualização geral e geração de relatórios (Gestor).
  - **`data/`** – Lógica de persistência local com SQLite (DBHelper).
  - **`listeners/`** – Interfaces de callback entre Fragments e Activities.
  - **`model/`** – Entidades de domínio como Action, Activity, UserRole, etc.
  - **`MainActivity.kt`** – Tela de login e redirecionamento por tipo de usuário.

- **`res/`**  
  Pasta de recursos visuais e de configuração do app:
  - **`layout/`** – Arquivos XML responsáveis pela construção das telas e componentes da interface.
  - **`drawable/`** – Imagens, vetores, formas e outros recursos gráficos.
  - **`color/`** – Efeito de troca de cor no menu inferior.
  - **`font/`** – Fontes personalizadas usadas na interface.
  - **`menu/`** – Arquivos XML de menus da aplicação.
  - **`mipmap/`** – Ícones do aplicativo em diferentes resoluções.
  - **`navigation/`** – Arquivos de navegação.
  - **`values/`** – Arquivos como `strings.xml` (textos), `themes.xml` (temas) e `colors.xml` (cores).
  - **`xml/`** – Arquivos de configuração diversos (ex.: preferências, estilos personalizados).

- **`Gradle Scripts/`**  
  Arquivos de configuração do projeto, gerenciamento de dependências e parâmetros de build.

---

## Contribuições dos Membros do Grupo

- [Caio Barros](https://github.com/caiobarrosmelo)
- Cauan Warley
- [Edmundo Duarte](https://github.com/Edmundo91)
- [Filipe Macedo](https://github.com/filipe-macedo)
- [Gabriel Alves](https://github.com/Gabrxs)
- [Gabriel Coelho](https://github.com/Biieru)

---

## Links para Demais Artefatos Entregues

> Aqui serão disponibilizados os links para documentos relacionados, como:
> - [Documento de requisitos](https://www.notion.so/Especifica-o-de-Requisitos-do-Sistema-1b965e19a7b3805e8a66e4fadebe0748)
> - [Relatório](https://acute-sternum-e26.notion.site/Relat-rio-Final-Unidade-de-Extens-o-2065d780990580d78c36ecb13f76c625?source=copy_link)
> - [Protótipo](https://www.figma.com/proto/VKRKXyLgakN3exJ1mfqr1s/Projeto-3?node-id=32-6&p=f&t=DGhV8zuVcv1GAs9W-1&scaling=scale-down&content-scaling=fixed&page-id=32%3A3&starting-point-node-id=32%3A6)
> - [Pesquisa UX](https://miro.com/app/board/uXjVIAXIpJo=/?share_link_id=795294644843)

---
