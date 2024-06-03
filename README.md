# AutoDocAI
Este projeto de inteligência artificial se integra com openai e PGVector para proporcionar interações inteligentes em um sistema de chat. Ele é capaz de executar a geração de documentação de forma automatizada, utilizando recursos de processamento de linguagem natural para criar documentações detalhadas sobre arquivos e pastas de um projeto. O objetivo principal dessa integração é facilitar a organização e o compartilhamento de informações relevantes sobre os projetos em questão.
Os usuários conseguem interagir com o frontend do sistema de chat por meio de mensagens, onde podem solicitar a geração de documentação para arquivos específicos, pastas inteiras ou até mesmo para o projeto como um todo. A inteligência artificial presente no sistema processa essas solicitações, utiliza algoritmos de geração de documentação e retorna as informações desejadas aos usuários de forma clara e objetiva. Essa interação possibilita aos usuários acessar documentações detalhadas sem a necessidade de realizar o processo manualmente, agilizando assim o entendimento e a utilização dos recursos disponíveis no projeto.

![image](https://github.com/arthurparahyba/autodocai/assets/5795841/19956ce2-90f7-403f-9cf2-9c3d1928900e)


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
Na aplicação, existe o conceito de projeto que é basicamente uma pasta dentro do sistema operacional.
Ao cadastrar um projeto, a aplicação inicia a varredura da pasta lendo dos os arquivos e pastas do projeto. É assim que a documentação é gerada. Cada arquivo tem seu conteúdo em texto lido, enviado para o chatgpt
onde um resumo sobre aquele arquivo é gerado. O prompt prompt-generate-file-doc.txt é quem descreve como esse resumo é gerado. 
Após a geração de resumos de todos os arquivos dentro de uma pasta, outro prompt gera um resumo da pasta em si. O prompt no arquivo prompt-generate-folder-doc.txt é usado exatamente para isso.
Cada resumo de um arquivo e pasta é enviado para um banco de dados vetorial, no caso o PGVector. O Spring AI permite a integração com vários tipos de bancos de dados vetoriais de forma bastante simplificada.
Quando o arquivo é enviado para o banco, o próprio Spring AI se encarrega de gerar os embeddings associados a cada arquivo. Segue um print do banco de dados com os resumos e seus embeddings.

![image](https://github.com/arthurparahyba/autodocai/assets/5795841/d601939d-bad7-4cc0-98fc-16e68c006b6d)

Como se pode ver, o banco de dados já é criado pelo Spring AI com estas quatro colunas. A coluna metadata é usada para filtrar os documentos. Cada documento é associado através do metadada com seu respectivo projeto, de forma que, ao consultar informações no PGVector sobre um projeto, não venham dados de outros projetos.
Quando toda a documentação é gerada e salva no bando de dados vetorial PgVector, o usuário pode utilizar a interface de chat para obter informações sobre o projeto.

Existe uma funcinalidade no Spring AI que permite que o ChatGPT acesse funcionalidades da aplicação utilizando Spring Function. Uma função chamada GetFileContentByPath foi criada para que o chatgpt consiga acessar determinados arquivos caso seja necessário ao processar uma mensagem do usuário. Por exemplo. Caso o usuário pergunte: do que se trata este projeto? Ora, utilizando a documentação que foi gerada e posteriormente persistida no PGVector, é possivel gerar uma respsota a essa pergunta, mas caso a pergunta seja muito específica, como: mostre a implementação do arquivo prompt-generate-file-doc.txt. Neste caso, o chatgpt precisará acessar o arquivo e esta função, este agent, fornece essa possibilidade durante o processamento de uma pergunta pelo chatgpt:

![image](https://github.com/arthurparahyba/autodocai/assets/5795841/7cf2509e-c5b3-448e-9c26-ab85e016f4cb)



## Interface
Uma interface gráfica simplificada em Thymeleaf e HTMX foi criada para que o usuário possa cadastrar projetos e acessar um chat sobre cada projeto. O chat se comunica com o backend via protocolo STOMP. Nele são apresentadas para o usuário todas documentações que estão sendo geradas.
Ao final, uma mensagem informa ao usuário que já é possível interagir através de perguntas sobre o projeto, arquivos etc.

## Pontos de Melhoria
1. Os prompts foram descritos sem muita atenção aos detalhes. Melhorar os prompts de arquivo e pastas, melhora substancialmente as respostas geradas. 
2. É possível evoluir o projeto para gerar outros tipos de documentações para tipos de arquivos diferentes, documentações com formatação diferente etc. O céu é o limite.



