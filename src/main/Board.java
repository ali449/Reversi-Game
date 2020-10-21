package main;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class Board implements Initializable
{
    @FXML
    TilePane squares;

    @FXML
    private CheckBox newWin;

    @FXML
    private Label status;

    @FXML
    Text blkScore, whtScore;

    @FXML
    Button newGameBtn;

    public ImageView img;//For create empty tiles

    public Game game = new Game();

    private boolean flag = false;

    public boolean mode = false;

    public Board()
    {

    }

    public static Board copy(Board b)
    {
        Board b1 = new Board();

        b1.game = Game.copy(b.game);
        b1.squares = b.squares;
        b1.flag = b.flag;
        b1.mode = b.mode;
        b1.img = b.img;
        b1.newWin = new CheckBox();

        return b1;
    }

    //Start function
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        //Create 64 tiles
        for(int i = 0; i < 64; i++)
        {
            img = new ImageView(new Image("main/empty.png"));

            squares.getChildren().add(img);

            game.states[i] = Game.State.EMPTY;
        }

        //Set 4 squares
        initFirstSquares();

        newGameBtn.setOnMouseClicked(event -> on_newGameBtn_clicked());

        setLegals(game.getLegalMoves());

        squares.setOnMouseClicked(event ->
        {
            if(flag && !newWin.isSelected())
            {
                ArrayList<Move> arr = game.getLegalMoves();

                if (arr.size() < 1)//If no legal move found
                {
                    System.out.println("No legal move found!");
                }
                else
                    setLegals(arr);
            }
        });

    }

    /**Set images legal moves
     * Call setMove() if a square clicked
    */
    public void setLegals(ArrayList<Move> arr)
    {
        flag = true;

        for(int i = 0; i < arr.size(); i++)
        {
            img = new ImageView(new Image("main/legal.png"));
            squares.getChildren().set(arr.get(i).target, img);

            final int position = i;
            img.setOnMouseClicked(event ->
            {
                if(newWin.isSelected())
                {
                    try {
                        Board br = Board.copy(this);
                        setMove(br, arr.get(position));
                        show(br.game);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else
                {
                    clearOtherLegals(this, game, arr, position);
                    setMove(this, arr.get(position));
                    this.game.flip();
                    status.setText(game.gameState.toString());

                    //Automatic move
                    if(mode)
                    {
                        Thread thread = new Thread(() ->
                        {
                            try
                            {
                                Thread.sleep(1500);
                                Platform.runLater(() -> autoMove(this));
                            } catch (InterruptedException e)
                            {
                                System.out.println("Error in thread");
                                e.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                }
            });
        }
    }

    //Decide autoMove and setMove(Max move)
    private void autoMove(Board board)
    {
        ArrayList<Move> legals = board.game.getLegalMoves();

        if(legals.size() < 1)
        {
            System.out.println("No auto legal move found!");
            return;
        }

        //Get Max Move
        Move m = Util.getMaxTarget(legals);

        clearOtherLegals(board, board.game, legals, legals.indexOf(m));

        setMove(board, m);
        game.flip();
        status.setText(board.game.gameState.toString());

        setLegals(board.game.getLegalMoves());
    }

    /*
        Set other imageView that not clicked, to empty.png.
        Call setMove() if one target has two or more source.
     */
    private void clearOtherLegals(Board b, Game game1, ArrayList<Move> arr, int position)
    {
        for(int j = 0; j < arr.size(); j++)
        {
            if(arr.get(j).target == arr.get(position).target)
                setMove(this, arr.get(j));

                //Change state other legal positions
            else if(j != position && game1.states[arr.get(j).target].equals(Game.State.EMPTY))
            {
                img = new ImageView(new Image("main/empty.png"));
                b.squares.getChildren().set(arr.get(j).target, img);
                game1.states[arr.get(j).target] = Game.State.EMPTY;
            }
        }
    }

    //Change color squares according to move
    private void setMove(Board board, Move move)
    {
        Game game1 = board.game;

        boolean state = game1.gameState.equals(Game.State.WHITE);

        //Contains next valid move in current direction
        int j = Game.getValidDir(move.type, move.source);

        if(j == -1)
            return;

        //Change squares color from move.source to move.target
        while(true)
        {
            if(j == -1)
                continue;

            String color = state ? "white" : "black";

            if(!newWin.isSelected())
            {
                ImageView im = new ImageView(new Image("main/" + color +".png"));

                RotateTransition rt = new RotateTransition(Duration.millis(700), im);
                rt.setByAngle(90);
                rt.play();

                board.squares.getChildren().set(j, im);
            }

            if(state)//If color = White
            {
                if(Util.findIndex(game1, j, true) == -1)
                {
                    game1.whtPos.add(j);
                    game1.states[j] = Game.State.WHITE;
                }

                int index = Util.findIndex(game1, j, false);
                if(index != -1)
                    game1.blkPos.remove(index);
            }
            else
            {
                if(Util.findIndex(game1, j, false) == -1)
                {
                    game1.blkPos.add(j);
                    game1.states[j] = Game.State.BLACK;
                }

                int index = Util.findIndex(game1, j, true);
                if(index != -1)
                    game1.whtPos.remove(index);

            }

            if(j == move.target)
                break;

            //Get next valid move by current direction
            j = Game.getValidDir(move.type, j);
        }

        updateScores(board);
    }

    //Compute and update players scores
    private void updateScores(Board b)
    {
        float blackScore = b.game.blkPos.size();
        float whiteScore = b.game.whtPos.size();

        //Convert score to percentage
        int pScore = (int)(blackScore / (blackScore + whiteScore) * 100);

        if(b.blkScore != null)
            b.blkScore.setText(String.valueOf((int)blackScore) + " _ " + String.valueOf(pScore) + "%");

        pScore = Math.abs(100 - pScore);

        if(b.whtScore != null)
            b.whtScore.setText(String.valueOf((int)whiteScore) + " _ " + String.valueOf(pScore) + "%");

        //Check who won?
        if((blackScore + whiteScore) == 64)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("End game");
            alert.setHeaderText(null);

            if(blackScore > whiteScore)
                alert.setContentText("Black won");
            else if(blackScore < whiteScore)
                alert.setContentText("White won");
            else
                alert.setContentText("Equal");

            alert.show();
        }

    }

    //Get game object and show it in new Board
    private Stage show(Game g1) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResource("board.fxml").openStream());

        Board b1 = loader.getController();

        TilePane s = b1.squares;
        for(int i = 0; i < 64; i++)
        {
            s.getChildren().set(i, new ImageView(new Image("main/" + g1.states[i].toString().toLowerCase() + ".png")));
        }

        b1.game = g1;
        b1.game.flip();
        b1.status.setText(b1.game.gameState.toString());
        b1.setLegals(b1.game.getLegalMoves());
        b1.updateScores(b1);

        Stage stage = new Stage();
        stage.setTitle("Reversi");

        Scene scene = new Scene(root, 450, 360);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        return stage;
    }

    private void on_newGameBtn_clicked()
    {
        squares.getChildren().clear();
        game.whtPos.clear();
        game.blkPos.clear();

        NewGame newGame = new NewGame();
        newGame.show();

        ResourceBundle r = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                return null;
            }

            @Override
            public Enumeration<String> getKeys() {
                return null;
            }
        };

        URL u;
        try {
            u = new URL("http://test.co");
            initialize(u, r);
        }catch (Exception e){
            e.printStackTrace();
        }

        newGame.startBtn.setOnMouseClicked(event ->
        {
            game.gameState = newGame.blkBtn.isSelected() ? Game.State.BLACK : Game.State.WHITE;
            mode = newGame.autoBtn.isSelected();
            newGame.close();
        });

    }

    private void initFirstSquares()
    {
        squares.getChildren().set(27, new ImageView(new Image("main/white.png")));
        squares.getChildren().set(36, new ImageView(new Image("main/white.png")));
        squares.getChildren().set(28, new ImageView(new Image("main/black.png")));
        squares.getChildren().set(35, new ImageView(new Image("main/black.png")));


        game.states[27] = Game.State.WHITE;
        game.states[36] = Game.State.WHITE;
        game.states[28] = Game.State.BLACK;
        game.states[35] = Game.State.BLACK;

        game.whtPos.add(27);
        game.whtPos.add(36);
        game.blkPos.add(28);
        game.blkPos.add(35);

        status.setText(game.gameState.toString());

    }

}
