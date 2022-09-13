package com.hohm;

import com.hohm.model.MemeRoom;

import static com.hohm.controller.GameBuilder.player;
import static com.hohm.controller.GameBuilder.rooms;

import com.hohm.model.Player;
import com.hohm.controller.TextInteractor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.hohm.utility.JsonParser.objMapFromFile;
import static com.hohm.utility.JsonParser.playerFromFile;
import static com.hohm.controller.TextInteractor.go;
import static org.junit.jupiter.api.Assertions.*;

class TextInteractorTest {

    @Test
    void goSuccess() throws IOException {
        rooms = objMapFromFile("rooms.json", MemeRoom.class);
        player = playerFromFile("characters.json");
        go("kitchen",rooms.get("hallway"));
        assertEquals ("kitchen", player.getRoom());
    }

    @Test
    void goFailure() throws IOException {
        rooms = objMapFromFile("rooms.json", MemeRoom.class);
        player = playerFromFile("characters.json");
        go("basement",rooms.get("hallway"));
        //Player cannot go to basement, player stays in the hallway
        assertEquals("hallway",player.getRoom());
    }

    @Test
    void getSuccess() throws IOException {
        rooms = objMapFromFile("rooms.json", MemeRoom.class);
        String[] inventory = {"bucket"};
        player = new Player("noob",inventory,"kitchen");
        //Completing the objective
        TextInteractor.use("use bucket", rooms.get("kitchen"));
        //Obtaining the item in the room after the objective is completed
        TextInteractor.get("get coffee",rooms.get("kitchen"));
        assertEquals("coffee mug", player.getItems()[0]);
    }

    @Test
    void useSuccess() throws IOException {
        rooms = objMapFromFile("rooms.json", MemeRoom.class);
        String[] inventory = {"bucket"};
        player = new Player("noob",inventory,"kitchen");
        TextInteractor.use("use bucket", rooms.get("kitchen"));
        //Players items are set to a string that looks like an empty array for playability
        assertEquals(player.getItems()[0], "[]");
    }

    @Test
    void checkCompleteTrue() throws IOException {
        rooms = objMapFromFile("rooms.json", MemeRoom.class);
        MemeRoom currentRoom = rooms.get("kitchen");
        currentRoom.getObjectives().get("itemFound").put("complete", "true");
        currentRoom.getObjectives().get("clueFound").put("complete", "true");
        TextInteractor.checkComplete(currentRoom);
        assertEquals(true,currentRoom.getComplete());
    }
}