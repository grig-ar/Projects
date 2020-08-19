package nsu.g16203.grigorovich;

class Controller  {
    public GameGUIField startNew(GameGUIField gamefield) {
        int x = gamefield.xCoord;
        int y = gamefield.yCoord;
        int mines = gamefield.mines;
        GameGUIField temp = new GameGUIField(x,y,mines);
        temp.images = gamefield.images;
        gamefield= null;
        return temp;
    }
    public boolean isVictory(GameGUIField gamefield) {
        return gamefield.isAllOpened();
    }
//    public void upd(GameGUIField gamefield) {
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                gamefield.clock++;
//            }
//        }, 1000,1000);
//
//    }

}
