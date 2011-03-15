/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package movielens;

/**
 *
 * @author kaldr
 */
public class test {

    public static void main(String args[]) {
        orII doc = new orII();
        orUser user = new orUser();
        String document = "movies.dat";
        //doc.readMovie(document);
        String ratingdoc = "test";
        user.readForItem(ratingdoc);
        Object[] b = user.userid2doc.keySet().toArray();

        for (int i = 0; i < 3; i++) {
            String c = b[i].toString();
            int dc=Integer.parseInt(c);
            System.out.println(user.userid2doc.get(dc));
        }

    }
}
