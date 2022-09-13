package com.hohm.controller;

import com.hohm.model.MemeLord;
import com.hohm.model.MemeRoom;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.hohm.controller.GameBuilder.player;
import static com.hohm.controller.GameLoop.reader;

/**
 * Authors: Kaitlyn Fernelius, Daniel An, Agustin Duran
 * BossEncounter is the class that holds the final boss battle information
 */
public class BossEncounter {

    static MemeLord boss = new MemeLord(20);
    static int spellCount = 3;
    static int playerAC = player.getHasAdvantage() ? 10: 8;

    /**
     * Roll takes in the dice size (i.e 20, 4, 6), and generates a random number in that range
     * @param diceMax setting max range of the dice roll
     * @return random number between 1 and diceMax
     */
    public static int roll(int diceMax){
        Random rand = new Random();
        int rollResult = rand.nextInt(diceMax + 1);
        rollResult = rollResult == 0 ? rollResult + 1 : rollResult;
        return rollResult;
    }

    /**
     * Encounter is the loop within the game loop for the final boss battle,
     * Who goes first is determined by a dice roll.
     * The loop breaks when either player has hp below 0
     * @param currentRoom - The current MemeRoom the player is in
     * @throws IOException
     * @throws InterruptedException
     */
    public static void encounter(MemeRoom currentRoom) throws IOException, InterruptedException {
        PrintSeparators.encounterSeparator();
        System.out.println(currentRoom.getDescription().get("encounterBegin"));

        //Roll to determine which player goes first
        int playerInt = roll(20);
        int bossInt = roll(20);

        if(playerInt > bossInt){
            System.out.println("\nGOOD NEWS EVERYONE...YOU ATTACK FIRST");
        }else{
            System.out.println("\nSorry but his roll was higher...MEME LORD ATTACKS FIRST");
        }
        System.out.print(">Press ENTER to Begin:");
        reader.readLine();

        //Start game loop for the encounter
        while (player.getHp() > 0 && boss.getHp() > 0){
            if(playerInt>bossInt){
                playerTurn();
                if(player.getHp() <= 0 || boss.getHp() <= 0){
                    break;
                }
                bossTurn();
                if(player.getHp() <= 0 || boss.getHp() <= 0){
                    break;
                }
            }else{
                bossTurn();
                if(player.getHp() <= 0 || boss.getHp() <= 0){
                    break;
                }
                playerTurn();
                if(player.getHp() <= 0 || boss.getHp() <= 0){
                    break;
                }

            }
        }

        //Room is set based on who won the encounter, if player doesn't win the room is set to "dead and the game ends
        if(boss.getHp() <= 0){
            player.setRoom("win");
        }else{
            player.setRoom("dead");
        }

    }

    /**
     * playerTurn handles the logic for the player turn, based on user input the player attacks with 2 different options
     * 1 = Sword attack, has a chance of missing
     * 2 = Spell attack, will always hit but player only has 3 spells to use
     * @throws IOException
     * @throws InterruptedException
     */
    public static void playerTurn() throws IOException, InterruptedException {
        PrintSeparators.encounterSeparator();
        String attack;
        while (true) {
            System.out.println("YOUR TURN");
            System.out.println("Choose Your Attack:");
            System.out.println("1) Roll Sword Attack");
            if (spellCount > 0) {
                System.out.printf("2) Roll Spell Attack (%s)%n", spellCount);
            }
            System.out.print(">");
            attack = reader.readLine();
            if (attack.equals("1") || attack.equals("2") || attack.equals("3")) {
                break;
            } else {
                System.out.println("Please enter a valid selection");
            }
        }

        if (attack.equals("1")) {
            System.out.println("Great Choice! Press Enter to roll your d20 to attempt to hit with your Sword: ");
            reader.readLine();
            System.out.println("Rolling.....");
            TimeUnit.SECONDS.sleep(1);
            int attackRoll = roll(20);
            if (attackRoll > 8) {
                System.out.printf("You rolled... %s YOU HIT!%n", attackRoll);
                System.out.println("Press Enter to Roll for Damage:");
                reader.readLine();
                int damageRoll = roll(4) + roll(4);
                boss.hit(damageRoll);
                if(boss.getHp() < 0){boss.setHp(0);}
                System.out.printf("You hit the enemy for %s damage%n", damageRoll);
                System.out.printf("The enemy has %s health left%n", boss.getHp());
                reader.readLine();
            }else{
                System.out.printf("You rolled... %s you missed!%n", attackRoll);
                System.out.println("It's the MemeLord's Turn");
                reader.readLine();
            }
        } else if (spellCount > 0) {
            System.out.println("Perfect, let's throw some magic his way AUTOMATIC HIT!!\nPress Enter to roll for damage");
            spellCount--;
            System.out.print(">");
            int damageRoll = roll(4) + roll(4);
            reader.readLine();
            boss.hit(damageRoll);
            if(boss.getHp()<0){boss.setHp(0);}
            System.out.printf("You hit the enemy for %s damage%n", damageRoll);
            System.out.printf("The enemy has %s health left%n", boss.getHp());
            reader.readLine();
        } else {
            System.out.println("Invalid selection, enter the number of your attack!");
        }

    }

    /**
     * The bossTurn determines the boss' roll,
     * The roll is compared to the players Armor Class
     * On a success the player loses health, on a failure no damage is incurred
     * @throws InterruptedException
     * @throws IOException
     */
    public static void bossTurn() throws InterruptedException, IOException {
        PrintSeparators.encounterSeparator();
        System.out.println("MEMELORD'S TURN");
        System.out.println("The MemeLord is rolling to hit: ");
        TimeUnit.SECONDS.sleep(1);
        int attackroll = roll(20);

        if(attackroll > playerAC){
            System.out.printf("ROLL: %s%n",attackroll);
            System.out.println("HE HITS, The MemeLord is rolling for damage:");
            TimeUnit.SECONDS.sleep(1);
            int damageRoll = roll(4) + roll(4);
            System.out.printf("Damage on hit: %s%n",damageRoll);
            int newhp = player.getHp() - damageRoll;
            if(newhp < 0){newhp = 0;}
            player.setHp(newhp);
            System.out.printf("You have %s hp left%n", player.getHp());
            reader.readLine();
        }else{
            System.out.println("The Roll did not meet your Armor Class, MISS, it's now your turn!");
            reader.readLine();
        }
    }


}
