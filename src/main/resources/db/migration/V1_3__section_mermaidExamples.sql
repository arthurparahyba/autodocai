INSERT INTO section (id, name, content)
VALUES (
2,
'mermaidExamples',
'

### Exemplos de geração de código Mermaid

### Instruções
1. Os dados fornecidos são fruto de uma consulta ao banco de dados e por causa de joins feito entre tabelas, alguns dados podem vir
duplicados, como atributos, métodos etc. Remova a duplicação desnecessária ao gerar os diagramas.

**Diagrama de classes com atributos**
Data:
packageName className	attributes
"br.com.escola.escola.model"    "Aluno" ["id: Long", "turma: Turma", "matricula: String", "nome: String"]
"br.com.escola.escola.model"    "Turma"	["id: Long", "nome: String", "alunos: Aluno"]
"br.com.escola.escola.model"    "Materia" []
Mermaid:
classDiagram:
    class Aluno {
        <<br.com.escola.escola.model>>
        +Long id
        +Turma turma
        +String matricula
        +String nome
    }

    class Turma {
        <<br.com.escola.escola.model>>
        +Long id
        +String nome
        +Aluno alunos
    }
    class Materia {
        <<br.com.escola.escola.model>>
    }

**Diagrama de classes com métodos**
Data:
packageName className	functions	parameters
"br.com.escola.escola.model"    "Turma"	"addAluno: void"	["Aluno"]
"br.com.escola.escola.model"    "Turma"	"setId: void"	["Long"]
"br.com.escola.escola.model"    "Turma"	"Turma: void"	["String"]
"br.com.escola.escola.model"    "Turma"	"setNome: void"	["String"]
"br.com.escola.escola.model"    "Turma"	"addMateria: void"	["Materia"]
Mermaid:
classDiagram
    class Turma {
        <<br.com.escola.escola.model>>
        +void addAluno(Aluno aluno)
        +void setId(Long id)
        +void Turma(String nome)
        +void setNome(String nome)
        +void addMateria(Materia materia)
    }

**Diagrama de classes com atributos e métodos**
Data:
packageName className	attributes  functions   parameters
"br.com.escola.escola.model"    "Turma"     []  null    null
"br.com.escola.escola.model"    "Materia"	["id: String", "nome: String", "codigo: String", "professores: Professor", "turmas: Turma"] addTurma    Turma
"br.com.escola.escola.model"    "Materia"	["id: String", "nome: String", "codigo: String", "professores: Professor", "turmas: Turma"] addProfessor    Professor
Mermaid:
classDiagram
    class Materia {
        <<br.com.escola.escola.model>>
        +String id
        +String nome
        +String codigo
        +Professor professores
        +Turma turmas
        +addProfessor(Professor)
        +addTurma(Turma)
    }
    class Turma {
        <<br.com.escola.escola.model>>
    }


');