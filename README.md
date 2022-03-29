# Redis com spring webflux

## Bibliotecas java redis

### Jedis
- Rapido
- Não escalavel e não thread safe

### Lettuce
- Escalável
- Suporte reactive streams

### Redisson
- Escalável
- Suporte reactive streams
- Melhor abstração
- Mais recursos

### Cache local com cache remoto
- podemos ter o cache local na aplicação e o cache remoto, quando o cache local e modificado, este e sincronizado com o remoto e outra aplicação terá o valor do cache atualizado
- para isso temos algumas estratégias, como UPDATE (que execute o processo acima), NONE(que não executa).
- temos tambem a estratégia de reconectação, onde podemos manter o cache local (none) ou limpar (clear), quando perdermos a conexão com o servidor redis