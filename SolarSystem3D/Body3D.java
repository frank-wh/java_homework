package session13;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

public class Body3D {
    public static void main(String[] args) throws FileNotFoundException{
        /* read data from solarsystem.dat */
        ArrayList<Body> bodies = new ArrayList<>();
        for(int i = 0; i < 14; i++)
            bodies.add(Body.readFile(i));
        /* only 8 objects(7 planets, 1 satellite - the moon) have acceleration */
        for(int i = 0; i < 8; i++){
            Body.Vec3d a = new Body.Vec3d();
            for(int j = 1; j < 8; j++){
                double m = bodies.get(j).getMass();
                Body.Vec3d pos = bodies.get(j).getPosition();
                Body.Vec3d targetPos = bodies.get(i).getPosition();
                Body.Vec3d aBetween = Body.calcu(m, pos, targetPos);
                a = Body.Vec3d.add(a, aBetween);
            }
            bodies.get(i).setAcce(a);
        }
        /* calculate moon's position seperately because moon is not a planet */
        Body.Vec3d p = Body.Vec3d.add(bodies.get(4).getPosition() , bodies.get(3).getPosition());
        bodies.get(4).setPosition(p);
        /* use iterator to print out all the information */
        for(Iterator<Body> i = bodies.iterator(); i.hasNext(); )
            System.out.println(i.next());
    }
}
