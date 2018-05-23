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

<img src="presentation/assets/img/GameServerArchitecture.png" alt="exception" style="width: 900px;"/>

---
## Agenda
0. Game architecture
0. **[Game client]**
0. Game server

---
## Game client
Game client is a separate HTML5 project (js+canvas)  
https://github.com/rybalkinsd/atom-bomberman-frontend  
Check it out

---
### Instances
- Player
- Bomb
- Fire
- Tile
- Bonus

---
### Front infrastructure
- GameEngine - basic mechanics and render
- InputEngine - input handling 
- ClusterSettings - infrastructure settings

---
**Оставьте обратную связь**
(вам на почту придет анкета)  

**Это важно!**
