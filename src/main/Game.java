package main;

import java.util.ArrayList;

public class Game
{
    public enum State
    {
        BLACK, WHITE, LEGAL, EMPTY
    }
    public State[] states = new State[64];

    public State gameState;

    public ArrayList<Integer> blkPos = new ArrayList<>();
    public ArrayList<Integer> whtPos = new ArrayList<>();

    public Game()
    {
        gameState = State.BLACK;
    }

    //Copy a game by Value, not reference
    public static Game copy(Game g)
    {
        Game g1 = new Game();

        for(int i = 0; i < 64; i++)
            g1.states[i] = g.states[i];
        g1.gameState = g.gameState;

        g1.blkPos = new ArrayList<>();
        g1.whtPos = new ArrayList<>();

        for (Integer a: g.blkPos)
            g1.blkPos.add(a);
        for(Integer a: g.whtPos)
            g1.whtPos.add(a);

        return g1;
    }

    //Change game state
    public void flip()
    {
        gameState = gameState.equals(State.BLACK) ? State.WHITE : State.BLACK;
    }

    public ArrayList<Move> getLegalMoves()
    {

        /*for(int i = 0; i < blkPos.size(); i++)
            System.out.print(blkPos.get(i) + " " + states[blkPos.get(i)] + "  ");
        System.out.println("white");
        for(int i = 0; i < whtPos.size(); i++)
            System.out.print(whtPos.get(i) + " " + states[whtPos.get(i)] + "  ");
        System.out.println();
        System.out.println("end");*/

        ArrayList<Move> moves = new ArrayList<>();

        boolean flip = true;//White state
        State enemy = State.BLACK;

        if (gameState.equals(State.BLACK))
        {
            flip = false;
            enemy = State.WHITE;
        }

        int size = flip ? whtPos.size() : blkPos.size();
        for (int i = 0; i < size; i++)
        {
            int source = flip ? whtPos.get(i) : blkPos.get(i);

            for (int j = 0; j < 8; j++)
            {

                int nextPos = getValidDir(j, source);

                if (nextPos == -1)
                {
                    continue;
                }

                if(states[nextPos].equals(enemy))
                {
                    while(true)
                    {
                        nextPos = getValidDir(j, nextPos);

                        if(nextPos == -1)
                        {
                            break;
                        }

                        if(states[nextPos].equals(gameState))
                            break;

                        if(states[nextPos].equals(State.EMPTY))
                        {
                            moves.add(new Move(source, nextPos, j));
                            break;
                        }
                    }
                }
            }
        }

        return moves;
    }

    //Get next valid direction
    public static int getValidDir(int j, int pos)
    {
        //@param a[0] = row, a[1] = column
        int[] a = getTwoPos(pos);

        int result = -1;
        switch (j)
        {
            case 0:
                result = getOnePos(a[0], a[1] + 1);
                result = a[1] + 1 > 7 ? -1 : result;
                break;
            case 1:
                result = getOnePos(a[0], a[1] - 1);
                result = a[1] - 1 < 0 ? -1 : result;
                break;
            case 2:
                result = getOnePos(a[0] + 1, a[1]);
                result = a[0] + 1 > 7 ? -1 : result;
                break;
            case 3:
                result = getOnePos(a[0] - 1, a[1]);
                result = a[0] - 1 < 0 ? -1 : result;
                break;
            case 4:
                result = getOnePos(a[0] + 1, a[1] + 1);
                result = a[0] + 1 > 7  || a[1] + 1 > 7 ? -1 : result;
                break;
            case 5:
                result = getOnePos(a[0] + 1, a[1] - 1);
                result = a[0] + 1 > 7  || a[1] - 1 < 0 ? -1 : result;
                break;
            case 6:
                result = getOnePos(a[0] - 1, a[1] + 1);
                result = a[0] - 1 < 0  || a[1] + 1 > 7 ? -1 : result;
                break;
            case 7:
                result = getOnePos(a[0] - 1, a[1] - 1);
                result = a[0] - 1 < 0  || a[1] - 1 < 0 ? -1 : result;
                break;
        }

        return result;
    }

    //Convert two dimension array to one
    private static int getOnePos(int row, int col)
    {
        return (row * 8 + col);
    }

    //Convert one dimension array to two
    private static int[] getTwoPos(int index)
    {
        int[] a = new int[2];
        a[0] = index / 8;//row
        a[1] = index % 8;//column
        return a;
    }

}
