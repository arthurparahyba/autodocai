INSERT INTO prompt_spec (id, name, content) VALUES (
16,
'generate-modules-by-classes',
'
### Exemplos de como identificar módulos de uma aplicação com base no nome das classes e seus pacotes:
Q: Quais os módulos das classes a seguir e quais as classes pertencem a cada módulo?
Classes:
- ClassName: Item, PackageName: br.com.myssystem.model
- ClassName: Section, PackageName: br.com.myssystem.model
- ClassName: Group, PackageName: br.com.myssystem.model
R: Módulos encontrados:
Módulo: myssystem
- ClassName: Item, PackageName: br.com.myssystem.model
- ClassName: Section, PackageName: br.com.myssystem.model
- ClassName: Group, PackageName: br.com.myssystem.model

Q: Quais os módulos das classes a seguir e quais as classes pertencem a cada módulo?
Classes:
- ClassName: Item, PackageName: br.com.myssystem.cart.model
- ClassName: Section, PackageName: br.com.myssystem.cart.model
- ClassName: Sell, PackageName: br.com.myssystem.buy.model
- ClassName: ItemByTime, PackageName: br.com.myssystem.buy.model
R: Módulos encontrados:
Módulo: cart
- ClassName: Item, PackageName: br.com.myssystem.cart.model
- ClassName: Section, PackageName: br.com.myssystem.cart.model
Módulo: buy
- ClassName: Sell, PackageName: br.com.myssystem.buy.model
- ClassName: ItemByTime, PackageName: br.com.myssystem.buy.model

Q: Quais os módulos das classes a seguir e quais as classes pertencem a cada módulo?
Classes:
- ClassName: Item, PackageName: br.com.myssystem.cart.model
- ClassName: Section, PackageName: br.com.myssystem.cart.model
- ClassName: SectionEntity, PackageName: br.com.myssystem.cart.repository
- ClassName: ItemEntity, PackageName: br.com.myssystem.cart.repository
- ClassName: SectionRepository, PackageName: br.com.myssystem.cart.repository
- ClassName: SectionService, PackageName: br.com.myssystem.cart.service
- ClassName: AppController, PackageName: br.com.myssystem.cart.controller
- ClassName: SectionDTO, PackageName: br.com.myssystem.cart.dto
R: Módulos encontrados:
Módulo: cart
- ClassName: Item, PackageName: br.com.myssystem.cart.model
- ClassName: Section, PackageName: br.com.myssystem.cart.model
- ClassName: SectionEntity, PackageName: br.com.myssystem.cart.repository
- ClassName: ItemEntity, PackageName: br.com.myssystem.cart.repository
- ClassName: SectionRepository, PackageName: br.com.myssystem.cart.repository
- ClassName: SectionService, PackageName: br.com.myssystem.cart.service
- ClassName: AppController, PackageName: br.com.myssystem.cart.controller
- ClassName: SectionDTO, PackageName: br.com.myssystem.cart.dto

Considerando os exemplos anteriores, quais módulos existem considerando as classes a seguir?

### Classes
{classes}

### Schema json que deve ser usado para gerar a resposta:
{format}

');
