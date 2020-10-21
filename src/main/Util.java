package main;

import java.util.ArrayList;

public class Util
{
    /** Decide max move.
     * @param legalMoves Get current legal move and compute max move
     * @return return a move that give us max piece
     */
    public static Move getMaxTarget(ArrayList<Move> legalMoves)
    {
        int count = 0, max = 0;
        Move maxMove = new Move(0, 0, 0);

        int next;
        int i = 0;
        for( ; i < legalMoves.size(); i++)
        {
            //Get next position in current direction
            next = Game.getValidDir(legalMoves.get(i).type, legalMoves.get(i).source);

            while (true)
            {
                if(next != -1)
                    count++;

                if(next == legalMoves.get(i).target)
                {
                    if(max < count)
                    {
                        maxMove = legalMoves.get(i);
                        max = count;
                    }
                    count = 0;
                    break;
                }
                else
                    next = Game.getValidDir(legalMoves.get(i).type, next);
            }
        }

        return maxMove;
    }

    /**Get index of piece position
     @param color equals true for white
     @return index of piece position
     */
    public static int findIndex(Game game1, int position, boolean color)
    {
        if(color)
            for(int i = 0; i < game1.whtPos.size(); i++)
            {
                if (game1.whtPos.get(i).equals(position))
                    return i;
            }
        else
            for(int j = 0; j < game1.blkPos.size(); j++)
                if(game1.blkPos.get(j).equals(position))
                    return j;

        //System.out.println(a + " not found in " + b);
        return -1;
    }

}
