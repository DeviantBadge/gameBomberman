---
# Java
## Presentation of the final project
Made by: 
- Vorobyov Evgeniy 
- Udovichenko Danil 
- Volcovich Maksim

---
## Отметьтесь на портале
https://sphere.mail.ru/

---

## get ready

---
## Agenda
0. **[Game architecture]**
0. Game client
0. Game server
---
## Game Server
Game Server is a application that do in cycle:
0. get`s orders from multiple clients
0. play game mechanics
0. send game state to clients (replica)

---
### Server structure looks like this: 
<img src="presentation/assets/img/GameServerArchitecture.png" alt="exception" style="width: 900px;"/>

---
## Agenda
0. Game architecture
0. **[Game client]**
0. Game server
0. Game mechanics

---
## Game client
- We didn`t changed game client a lot, it still similar to
https://github.com/rybalkinsd/atom-bomberman-frontend  
- changes affected only Cluster settings.

---
### Instances
- Player
- Bomb
- Fire
- Tile
- Bonus

---

## Agenda
0. Game architecture
0. Game client
0. **[Game server]**
0. Game mechanics

---

## Game server
Game server now can create sessions with parameters

Such as - Player bonuses on start, bomb mechanics and a lot more 

---


**Оставьте обратную связь**
(вам на почту придет анкета)  

**Это важно!**
