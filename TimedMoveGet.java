import java.util.LinkedList;

class TimedMoveGet implements Runnable {
	
	int iteration = 1;
	Fanorona game;
	long limit;
	String bestMove;
	Piece.Type player;
	long start;
	
	public TimedMoveGet(Fanorona _game,long _start, long _limit, Piece.Type _player){
		game = _game;
		limit = _limit;
		bestMove = "";
		player = _player;
		start = _start;
	}
	
    private volatile boolean killed = false;

    public void run() {
        while (!killed) {
            try {
            	System.out.println("Doing iteration "+iteration);
            	doOnce();
            } catch (InterruptedException ex) {
            	killed = true;
            }
        }
    }

    public void kill() { killed = true; }
    private void doOnce() throws InterruptedException { 
    	
    	if(iteration > 4 || (iteration > 2 && game.board.whiteMoves == "")){
    		throw new InterruptedException();
    	}
    	
    	Piece.Type otherPlayer = player;
		
		MiniMaxTree mmt = new MiniMaxTree(game.copyGame());
		mmt.processToDepth(iteration,start,limit);
		
		if(otherPlayer == Piece.Type.BLACK){
			int minimum = 100000;
			for(int i = 0; i < mmt.root.children.size();i++){
				if(mmt.root.children.get(i).getUtilityValue() < minimum){
					bestMove = mmt.root.children.get(i).getState().getMove(false);
					minimum = mmt.root.children.get(i).getUtilityValue();
				}
			}
		}
		else{
			int maximum = -100000;
			for(int i = 0; i < mmt.root.children.size();i++){
				if(mmt.root.children.get(i).getUtilityValue() > maximum){
					bestMove = mmt.root.children.get(i).getState().getMove(true);
					maximum = mmt.root.children.get(i).getUtilityValue();
				}
			}
		}
		System.out.println("Set best move as "+bestMove);
		iteration++;
    }
}