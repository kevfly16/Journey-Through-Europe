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
    private final Player player;
    private final City src;
    private final City dest;
    private final int roll;
    
    public Move(Player p, City d) {
        player = p;
        src = p.getCurrentPosition();
        dest = d;
        roll = p.getRoll();
    }
    
    public Move(Player p, City s, City d, int r) {
        player = p;
        src = s;
        dest = d;
        roll = r;
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
    
    public String toString() {
        return getPlayer().getName() + ": " + getSrc().getName() + " -> " + getDest().getName();
    }
    
    public String encode() {
        return getPlayer().getName() + "," + getSrc().getName() + "," + getDest().getName() + "," + getRoll();
    }
}
