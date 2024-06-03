INSERT INTO section (id, name, content)
VALUES (
2,
'mermaidExamples',
'
Q:
packageName className	attributes
com.example.model    EntityA ["id: Long", "attribute1: String", "attribute2: Boolean"]
com.example.model    EntityB	["id: Long", "name: String", "entityA: EntityA"]
com.example.model    EntityC []
R:
classDiagram
    class EntityA {
        <<com.example.model>>
        +Long id
        +String attribute1
        +Boolean attribute2
    }

    class EntityB {
        <<com.example.model>>
        +Long id
        +String name
        +EntityA entityA
    }
    class EntityC {
        <<com.example.model>>
    }

Q:
packageName className	functions	parameters
com.example.model    EntityA	addEntityA: void	["EntityA"]
com.example.model    EntityA	setId: void         ["Long"]
com.example.service    EntityB	setId: void   	["String"]
com.example.service    EntityB	setName: void   	["String"]
com.example.service    EntityB	addEntityC: void	["EntityC"]
R:
classDiagram
    class EntityA {
         <<com.example.model>>
         +void addEntityA(EntityA)
         +void setId(Long)
    }
    class EntityB {
        <<com.example.model>>
        +void setId(String)
        +void setName(String)
        +void addEntityC(EntityC)
    }

Q:
packageName className	attributes  functions   parameters
com.example.model    EntityB     []  null    null
com.example.model    EntityC	["id: String", "description: String", "code: String", "entityBs: EntityB", "relatedEntities: EntityA"] addEntityB    EntityB
com.example.model    EntityC	["id: String", "description: String", "code: String", "entityBs: EntityB", "relatedEntities: EntityA"] addRelatedEntity    EntityA
R:
classDiagram
    class EntityC {
        <<com.example.model>>
        +String id
        +String description
        +String code
        +EntityB entityBs
        +EntityA relatedEntities
        +void addEntityB(EntityB)
        +void addRelatedEntity(EntityA)
    }
    class EntityB {
        <<com.example.model>>
    }

Q:
moduleName
moduleOne
moduleTwo
moduleThree
R:
graph TD
    ModuleA[moduleOne]
    ModuleB[moduleTwo]
    ModuleC[moduleThree]

Q:
moduleName
myModule
R:
graph TD
    ModuleA[myModule]

');