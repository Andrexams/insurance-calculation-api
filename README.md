# insurance-calculation-api

### Para executar o projeto:
1. Realizar o download e instalação do OpenJDK 17
2. Abrir o projeto em sua IDE de preferência aqui foi usado o IntelliJ como exemplo
3. Após o término do download das dependências configurar a classe de execução conforme imagem abaixo:

![img.png](img.png)

4. Agora é executar pelo botão Run, ou Shift+F10

## Sobre a solução:

* O padrão utilizado foi a Arquitetura Hexagonal, o diagrama abaixo demonstra as camadas e a relação entre elas:

![img_2.png](img_2.png)

* Para realizar o cálculo foi utilizado o Design Pattner Strategy permitindo que cada produto
tenha a possibilidade de especializar sua fórmula de cálculo trazendo flexibilidade e extensibilidade para solução. Diagrama abaixo:

![img_1.png](img_1.png)

* Em relação aos impostos dos produtos, para simplificar foi utilizado uma enumeração(ProductTaxesEnum) onde temos
as taxas dos impostos por categoria de produto. Se o imposto não deve ser cobrado devee-se informar 0.0, se essa tabela estivesse persistida
em banco de dados os campos não permitiriam null.

![img_4.png](img_4.png)

* Relacionado a camada Rest, foi configurado o básico para atender os requisitos, foram tratadas mensagens
amigáveis para o usuário e retorno correto de status HTTP. Se fôssemos disponibilizar em algum ambiente é necessário implementação de segurança, docs(swagger).


* Relacionado a camada de persistência, por simplicidade, foi realizado o básico para persitir os produtos, o banco HSQLDB está embarcado e se reiniciar a aplicação
irá perder os dados. Se fôssemos disponibilizar em algum ambiente é necessário incluir a dependência do banco de dados escolhido, configurar as
propriedades como dialeto do hibernate, pool, string de conexão.


* Em relação aos testes, foi utilizado Mockito com JUnit para os testes das unidades menores, e, para
testar adapters, banco, rest foi utilizado SpringBootTest com JUnit obtendo total cobertura: 

![img_3.png](img_3.png)

* Sobre a observabilidade(métricas, traces e logs), esse tema é específico por produto(Datadog, Dynatrace, Grafana...), suas configurações de ambiente, bibliotecas, agents são diferentes, o Datadog por exemplo é necessário rodar a aplicação com um agent(APM) embarcado no processo,
incluir a dependência para a lib a qual disponibiliza métodos para trace e envio de métricas, os logs também precisam
ser configurados para serem vinculados aos traces e seu formato deve o que o provedor espera, no caso do Datadog
o formato em JSON, visto isso nessa solução foi colocado apenas o registro de erros em console usando o logging(SLFJ), e a classe
que intercepta e faz o registro é a RestControllerAdvice.