INSERT INTO prompt_spec (id, name, content) VALUES (
12,
'extract-attributes-from-code-validation',
'
Dada uma explicação no texto a seguir sobre quais atributos foram encontrados em um determinado código, me retorne se foram encontrados atributos ou não. Caso tenham sido encontrados, retorne a palavra IS_VALID entre aspas, caso contrário, retorne "IS_NOT_VALID"

###Texto
{beforeOutput}

');