package Battle;

import Players.KI.Bot;
import Players.Users.User;
import Pokemon.Pokemon;
import Pokemon.helper.Attacke;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * Die Klasse erlaubt einfache, textbasierte Kämpfe im Konsolenmodus.
 *
 */
public class Battle {
    private User user;
    private List<Pokemon> team1;
    private List<Pokemon> team2;
    private Pokemon p1;//Spieler
    private Pokemon p2;//KI
    private int roundCount = 0;
    private int turnsSinceSwitch = 0;
    private Bot bot;
    private boolean gameOver = false;
    private boolean isPlayerWinner = false; 

    /**
     * Erstellt ein neues {@code Battle}-Objekt mit zwei Teams und den jeweils aktiven Pokémon.
     */
    public Battle(List<Pokemon> pokemons, Bot bot) {
        this.team1 = pokemons;
         //
        this.p1 = getNextAvailable(team1);
        //
        this.bot = bot;
        this.team2 = new ArrayList<Pokemon>(bot.getTeam());
        this.p2 = bot.getaktuellesPokemon();
        
    }
    

    public String playerAttack(Attacke attack){
        
        StringBuilder result =new StringBuilder();
        Pokemon attacker = p1;
        Pokemon defender = p2;
        
        int damage1 = calculateDamage(attack, defender);
        defender.setHp(defender.getHp()-damage1);
        turnsSinceSwitch++;
        
        result.append(String.format("%s greift mit %s an. Schaden: %d (Wirkung: %f)\n",
            attacker.getName(), attack.getName(), damage1,
            defender.getMultiplikatorGegen(attack.getElement())));
        
        if (defender.isFainted()){
            result.append(defender.getName()).append(" ist K.O.!\n");
            team2.remove(defender);
            this.bot.removePokemon(defender);


            System.out.println("neu team 2.    "+team2);
            System.out.println("neu team 2 in the bottttt.    "+bot.getTeam());
            if (hasAvailablePokemon(team2)){
                p2 = getNextAvailable(team2);
                result.append("Gegner schickt ").append(p2.getName()).append(" in den Kampf.\n");
            }else{
                result.append("Du hast gewonnen!\n");
                gameOver = true; // to end the battle
                isPlayerWinner = true;
                System.out.println(" I m in player turn GameOver = " + gameOver + " Player"+ isPlayerWinner);
                turnsSinceSwitch = 0;
            }
        }  
        return result.toString();
    }
    public String KITurn(){
        StringBuilder result = new StringBuilder();

        Pokemon defender = p1; 
        
        p2 = bot.getBestPokemon(team2, defender);
        Pokemon attacker =  p2; 
        result.append("Bot schickt ").append(attacker.getName()).append(" in den Kampf.\n");
        Attacke kiAttack = bot.choosAttacke(attacker, defender);
        int damage = calculateDamage(kiAttack, defender);
        defender.setHp(defender.getHp() - damage);

        result.append(String.format("%s greift mit %s an. Schaden: %d (Wirkung: %.2f)\n",
            attacker.getName(), kiAttack.getName(), damage,
            defender.getMultiplikatorGegen(kiAttack.getElement())));

        if (defender.isFainted()){
            
            // Pokémon nicht aus Liste entfernen, sondern nur auf next available wechseln
            result.append(defender.getName()).append(" ist K.O.!\n");
            team1.remove(defender);
            System.out.println("neu team 1.  "+team1);
            // Spieler wählt nächstes Pokémon
            if (hasAvailablePokemon(team1)) {
                p1 = getNextAvailable(team1); 
                result.append("Du schickst ").append(p1.getName()).append(" in den Kampf.\n");
            } else {
                result.append("Du hast verloren!\n");
                //this.user.addLoss();
                gameOver = true;
                isPlayerWinner = false;
                System.out.println(" I m in Ki turn GameOver = " + gameOver + " Player"+ isPlayerWinner);

            }
            turnsSinceSwitch = 0;
        }

        roundCount++;
        return result.toString();
    }

    /**
     * Berechnet den Schaden einer bestimmten Attacke gegen ein gegnerisches Pokémon.
     * Dabei wird ein Typen-Multiplikator berücksichtigt.
     */
    public int calculateDamage(Attacke attacke, Pokemon pokemon) {
        double multiplier = pokemon.getMultiplikatorGegen(attacke.getElement());
        int rawDamage = attacke.getSchaden();
        return (int) (rawDamage * multiplier);
    }

    /**
     * Prüft, ob im Team noch mindestens ein kampffähiges (nicht besiegtes) Pokémon vorhanden ist.
     */
    private boolean hasAvailablePokemon(List<Pokemon> team) {
        return team.stream().anyMatch(p -> !p.isFainted());
    }

    private Pokemon getNextAvailable(List<Pokemon> team) {
        for (Pokemon p : team) {
            if (!p.isFainted()) {
                return p;
            }
        }
        return null;
    }

    public String playerSwitchPokemon(Pokemon p) {
        
        if (p.isFainted()) {
            return "Du kannst kein besiegtes Pokémon auswählen!\n";
        }
        this.p1 = p;
        turnsSinceSwitch = 0;
        return "Du hast auf " + p.getName() + " gewechselt.\n";
    }


    
    public boolean isGameOver() {
        return gameOver;
    }

    public Pokemon getPlayerPokemon() {
        return p1;
    }

    public Pokemon getEnemyPokemon() {
        return p2;
    }

    public boolean getisPlayerWinner() {
        return this.isPlayerWinner;
    }
    public List<Pokemon>getTeam1(){
        return team1;
    }
    public List<Pokemon>getTeam2(){
        return team2;
    }
}


