package ru.atom.chat.socket.util.jsoncheck;

import ru.atom.chat.socket.message.response.messagedata.Replica;
import ru.atom.chat.socket.objects.base.interfaces.Replicable;
import ru.atom.chat.socket.objects.base.util.Position;
import ru.atom.chat.socket.objects.ingame.*;
import ru.atom.chat.socket.util.JsonHelper;

import java.util.List;

public class JsonCreationCheck {

    public static void main(String[] args) {
        Position position = new Position(10,20);
        Pawn pawn = new Pawn(position);
        Bomb bomb = new Bomb(position,pawn);
        Bonus bonus1 = new Bonus(position, Bonus.BonusType.BOMBS);
        Bonus bonus2 = new Bonus(position, Bonus.BonusType.SPEED);
        Bonus bonus3 = new Bonus(position, Bonus.BonusType.RANGE);
        Fire fire = new Fire(position);
        Wall wall = new Wall(position);
        Wood wood = new Wood(position);
        Replica replica = new Replica();
        replica.addToReplica(pawn);
        replica.addToReplica(bomb);
        replica.addToReplica(bonus1);
        replica.addToReplica(bonus2);
        replica.addToReplica(bonus3);
        replica.addToReplica(fire);
        replica.addToReplica(wall);
        replica.addToReplica(wood);

        List<Replicable> replicables = replica.getData();
        replica.addAllToReplica(replicables);

        System.out.println(JsonHelper.toJson(position));
        System.out.println(JsonHelper.toJson(pawn));
        System.out.println(JsonHelper.toJson(bomb));
        System.out.println(JsonHelper.toJson(bonus1));
        System.out.println(JsonHelper.toJson(bonus2));
        System.out.println(JsonHelper.toJson(bonus3));
        System.out.println(JsonHelper.toJson(fire));
        System.out.println(JsonHelper.toJson(wall));
        System.out.println(JsonHelper.toJson(wood));
        System.out.println(JsonHelper.toJson(replica));

        replica.addAllToReplica(replicables);
        System.out.println(JsonHelper.toJson(replica.toString()));

        replica.addAllToReplica(replicables);
        System.out.println(replica.toString());
    }
}
