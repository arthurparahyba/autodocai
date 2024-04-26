# AutoDocAI
Este projeto de inteligência artificial se integra com openai e PGVector para proporcionar interações inteligentes em um sistema de chat. Ele é capaz de executar a geração de documentação de forma automatizada, utilizando recursos de processamento de linguagem natural para criar documentações detalhadas sobre arquivos e pastas de um projeto. O objetivo principal dessa integração é facilitar a organização e o compartilhamento de informações relevantes sobre os projetos em questão.
Os usuários conseguem interagir com o frontend do sistema de chat por meio de mensagens, onde podem solicitar a geração de documentação para arquivos específicos, pastas inteiras ou até mesmo para o projeto como um todo. A inteligência artificial presente no sistema processa essas solicitações, utiliza algoritmos de geração de documentação e retorna as informações desejadas aos usuários de forma clara e objetiva. Essa interação possibilita aos usuários acessar documentações detalhadas sem a necessidade de realizar o processo manualmente, agilizando assim o entendimento e a utilização dos recursos disponíveis no projeto.

## Execução
Docker:
1. Certifique-se de ter o Docker e o Docker Compose instalados em sua máquina.
2. Acesse o diretório do projeto onde está localizado o arquivo `docker-compose.yml`.
3. Execute o comando `docker-compose up` para iniciar os serviços definidos no arquivo.
Aplicação:
1. ./mvnw clean package
2. ./mvnw spring-boot:run
Dependência:
1. É preciso criar um token para comunicação com a openai. Após criar, adicione na variável de ambiente OPEN_AI_KEY que será lida pelo projeto.

## Tecnologias
1. **Thymeleaf**: Uma biblioteca que permite a renderização dinâmica de dados em templates HTML, utilizada para criar páginas web com conteúdo dinâmico.
2. **Tailwind CSS**: Um framework de CSS que oferece classes pré-estilizadas para facilitar a criação de layouts responsivos e modernos.
3. **HTMX**: Uma biblioteca JavaScript que possibilita requisições assíncronas e atualizações parciais do conteúdo de uma página, melhorando a interatividade e a experiência do usuário.
4. **SimpMessagingTemplate (STOMP e SockJS)**: Tecnologias utilizadas para comunicação em tempo real, permitindo a implementação de funcionalidades de chat ou atualizações dinâmicas nos projetos.
5. **Spring AI**:  Spring AI facilita a implementação de funcionalidades avançadas de processamento de linguagem natural, geração de respostas automáticas e interações inteligentes com usuários por meio de um chatbot.
6. **ChatGPT 3.5 Turbo**: O ChatGPT-3.5 Turbo é uma versão otimizada do modelo de linguagem GPT-3.5 da OpenAI, projetada para responder de maneira mais rápida e eficiente. 

## Geração de documentação
Existe o conceito de projeto. Um projeto é basicamente uma pasta dentro do sistema operacional
A geração de documentação é feita através da varredura de cada pasta e arquivo presente no projeto. A cada arquivo lido, o prompt descrito no arquivo prompt-generate-file-doc.txt é usado para gerar uma documentação resumida sobre aquele arquivo.
Quando todos os arquivos são lidos dentro de uma pasta, as documentações dos arquivos daquela pasta são usadas para gerar uma documentação mais abrangente sobre a pasta em si usando o prompt descrito no arquivo prompt-generate-folder-doc.txt.
Quando toda a documentação é gerada e salva no bando de dados vetorial PgVector, o usuário pode utilizar a interface de chat para obter informações sobre o projeto.
Um agente, utilizando Spring Function, permite que o chatgpt acesse determinados arquivos. Com isso, caso o usuário requisite o conteúdo de um arquivo, esta função é usada para acessar o arquivo e enviar os dados para o chatgpt.

## Interface
Uma interface gráfica simplificada em Thymeleaf e HTMX foi criada para que o usuário possa cadastrar projetos e acessar um chat sobre cada projeto. O chat se comunica com o backend via protocolo STOMP. Nele são apresentadas para o usuário todas documentações que estão sendo geradas.
Ao final, uma mensagem informa ao usuário que já é possível interagir através de perguntas sobre o projeto, arquivos etc.

## Pontos de Melhoria
Os prompts foram descritos sem muita atenção aos detalhes. Melhorar os prompts de arquivo e pastas, melhora substancialmente as respostas geradas. 
É possível evoluir o projeto para gerar outros tipos de documentações para tipos de arquivos diferentes, documentações com formatação diferente etc. O céu é o limite.
