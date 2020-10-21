package main;

public class Move
{
    int source;
    int target;

    public int type;//Direction of move
    /*
        case 0:
            Type = EE;
        case 1:
            Type = WW;
        case 2:
            Type = SS;
        case 3:
            Type = NN;
        case 4:
            Type = SE;
        case 5:
            Type = SW;
        case 6:
            Type = NE;
        case 7:
           Type = NW;
    */

    public Move(int source, int target, int type)
    {
        this.source = source;
        this.target = target;
        this.type = type;
    }

}
