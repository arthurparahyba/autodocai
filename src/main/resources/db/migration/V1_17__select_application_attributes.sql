INSERT INTO prompt_spec (id, name, content) VALUES (
9,
'select-application-attributes',
'
### Identificando o pacote da aplicação
Q: Qual o pacote da aplicação da classe br.com.escola.model.Materia?
R: br.com.escola
Q: Qual o pacote da aplicação da classe com.mysystem.module1.service.ItemAdapter?
R: com.mysystem
Q: Qual o pacote da aplicação da classe br.com.carbon.factory.ItemFactory?
R: br.com.carbon
Q: Qual o pacote da aplicação da classe br.com.carbon.factory.ItemFactory?
R: br.com.carbon

### Identificando pacotes dentro de outro:
Para identificar se um pacote A pertence a um pacote B, é preciso verificar se o texto correspondente ao pacote B é igual ao início do texto correspondente ao pacote A. Segue alguns exemplos:
Q: O pacote y.x.z.f pertence ao pacote x.y.z?
A: Não, pois y.x.z.f não começa com x.y.z
Q: O pacote a.b.c.d.t pertence ao pacote x.y.z?
A: Não, pois a.b.c.d.t não começa com x.y.z
Q: O pacote a.x.y.z.f.g pertence ao pacote x.y.z?
A: Não, pois a.x.y.z.f.g não começa com x.y.z
Q: O pacote x.y.z.a pertence ao pacote x.y.z?
A: Sim, pois x.y.z.a começa com x.y.z
Q: O pacote x.y1.z pertence ao pacote x.y.z?
A: Não, pois x.y1.z não começa com x.y.z
Q: O pacote x.y.z.x1.y1.z1 pertence ao pacote x.y.z?
A: Sim, pois  x.y.z.x1.y1.z1 começa com x.y.z

### Exemplo Perguntas:
Q: Quais atributos a seguir pertencem a mesma aplicação da classe que possui os atributos?
Atributos da classe br.com.escola.model.Materia:
- String id do pacote java.lang
- String nome do pacote java.lang
- String codigo do pacote java.lang
- Turma turma do pacote br.com.escola.sys1
- List<Turma> turmas do pacote java.util
- List<Professor> professores do pacote java.util
A: Primeiro é preciso identificar o pacote da aplicação da classe br.com.escola.model.Materia:
- br.com.escola
Depois é preciso identificar quais pacotes dos atributos pertencem ao pacote br.com.escola:
- String id do pacote java.lang (Nao pertence ao pacote br.com.escola)
- String nome do pacote java.lang (Nao pertence ao pacote br.com.escola)
- String codigo do pacote java.lang (Nao pertence ao pacote br.com.escola)
- Turma turma do pacote br.com.escola.sys1 (Pertence ao pacote br.com.escola)
- List<Turma> turmas do pacote java.util (Nao pertence ao pacote br.com.escola)
- List<Professor> professores do pacote java.util (Nao pertence ao pacote br.com.escola)
Lista de atributos que pertence a aplicação:
- Turma turma do pacote br.com.escola.sys1


Q: Quais atributos a seguir pertencem a mesma aplicação da classe que possui os atributos?
Atributos da classe br.com.escola.model.Materia:
- String codigo do pacote java.lang
- List<Turma> turmas do pacote java.util
- List<Professor> professores do pacote java.util
A: Primeiro é preciso identificar o pacote da aplicação da classe br.com.escola.model.Materia:
- br.com.escola
Depois é preciso identificar quais pacotes dos atributos pertencem ao pacote br.com.escola:
- String codigo do pacote java.lang (Nao pertence ao pacote br.com.escola)
- List<Turma> turmas do pacote java.util (Nao pertence ao pacote br.com.escola)
- List<Professor> professores do pacote java.util (Nao pertence ao pacote br.com.escola)
Lista de atributos que pertence a aplicação:
- Nenhum atributo encontrado


Q: Quais atributos a seguir pertencem a mesma aplicação da classe que possui os atributos?
Atributos da classe br.com.escola.model.Materia:
- String codigo do pacote java.lang
- Turma turma do pacote br.com.escola.module1.model
- Professor professor do pacote  com.escola.service
A: Primeiro é preciso identificar o pacote da aplicação da classe br.com.escola.model.Materia:
- br.com.escola
Depois é preciso identificar quais pacotes dos atributos pertencem ao pacote br.com.escola:
- String codigo do pacote java.lang (Nao pertence ao pacote br.com.escola)
- Turma turma do pacote br.com.escola.module1.model (Pertence ao pacote br.com.escola)
- Professor professor do pacote  com.escola.service (Pertence ao pacote br.com.escola)
Lista de atributos que pertence a aplicação:
- Turma turma do pacote br.com.escola.module1.model
### Fim Exemplos

Dado o que foi aprendido nos exemplos anteriores, liste os atributos que pertencem a aplicação na pergunta a seguir:

Q: Quais atributos a seguir pertencem a mesma aplicação da classe que possui os atributos?
{beforeOutput}
Vamos pensar passo a passo

');