/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

/**
 *
 * @author Kevin
 */
public class Move {
    private Player player;
    private City src;
    private City dest;
    private int roll;
    
    public Move(Player p, City d) {
        player = p;
        src = p.getCurrentPosition();
        dest = d;
        roll = p.getRoll();
    }
    
    public City getSrc() {
        return src;
    }
    
    public City getDest() {
        return dest;
    }
    
    public int getRoll() {
        return roll;
    }
    
    public Player getPlayer() {
        return player;
    }
}
