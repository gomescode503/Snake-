import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{				
		static final int SCREEN_WIDTH = 600;
		static final int SCREEN_HEIGTH = 600;
		static final int UNIT_SIZE = 25; //seta o tamanho dos objetos no jogo. visto atraves da matriz
		static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGTH)/UNIT_SIZE;	
		static final int DELAY = 100;
		final int x[] = new int[GAME_UNITS];
		final int y[] = new int[GAME_UNITS];
		int bodyParts = 6;
		int applesEaten;
		int appleX;
		int appleY;	
		char direction = 'R';
		boolean running = false;		
		Timer timer;
		Random random;
		
		
			
			GamePanel(){
				random = new Random();	
				this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGTH));
				this.setBackground(Color.black);
				this.setFocusable(true);
				this.addKeyListener(new MyKeyAdapter());
				startGame();
			}			
		
			public void startGame() {		
				newApple();
				running = true;
				timer = new Timer(DELAY, this);
				timer.start();			
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				draw(g);
				
			}
			
			public void draw(Graphics g) { //função que vai desenhar os objetos na tela					
					if(running){
						/*
						for(int i = 0; i < SCREEN_HEIGTH/UNIT_SIZE; i++) {
							g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGTH);
							g.drawLine(0, i*UNIT_SIZE, i*SCREEN_WIDTH, i*UNIT_SIZE);
						}
						*/			
						g.setColor(Color.red); //seta cor da maça
						g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //gera a forma da maça
						
						for(int i = 0; i < bodyParts; i++) {
							if(i == 0) {
								g.setColor(Color.green);
								g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
							}else {
								g.setColor(new Color(45, 180, 0)); //seta um tom diferente de verde em RGB
								//g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
								g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
							}			
						}
						g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
						g.setFont(new Font("Ink Free", Font.BOLD, 40));
						FontMetrics metrics = getFontMetrics(g.getFont());
						g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
					}else {
						gameOver(g);
					}			
							
			}
			
			public void newApple() {
				appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE; //posição x da maca
				appleY = random.nextInt((int)(SCREEN_HEIGTH/UNIT_SIZE)) * UNIT_SIZE;//posição y da maca
			}
			
			public void move() {
				for(int i = bodyParts; i > 0; i--) {
					x[i] = x[i -1];
					y[i] = y[i -1];
				}
				
				switch(direction) {
				case 'U': //up
					y[0] = y[0] - UNIT_SIZE;
					break;
				case 'D': //down
					y[0] = y[0] + UNIT_SIZE;
					break;
				case 'L': //left
					x[0] = x[0] - UNIT_SIZE;
					break;
				case 'R': //right
					x[0] = x[0] + UNIT_SIZE;
					break;
				}		
			}
			
			public void checkApple() {
				if((x[0] == appleX) && (y[0] == appleY)) {
					bodyParts++; //aumenta a cobra
					applesEaten++;
					newApple();
				}
				
			}
			
			public void checkCollisions() {
				for(int i = bodyParts; i > 0; i--) { //checa se a cobra bateu nela mesma
					if((x[0] == x[i]) && (y[0] == y[i])) {
						running = false;
					}
				}
				
				//checa se a cobra bateu na borda esquerda
				if(x[0] < 0) {
					running = false;
				}
				//checa se a cobra bateu na borda direita
				if(x[0] > SCREEN_WIDTH) {
					running = false;
				}
				//checa se a cobra bateu na borda de cima
				if(y[0] < 0) {
					running = false;
				}
				//checa se a cobra bateu na borda direita
				if(y[0] > SCREEN_HEIGTH) {
					running = false;
				}
				
				if(!running) { //para o timer
					timer.stop();
				}
			}
			
			public void gameOver(Graphics g) {
				

				//Game Over Text
				g.setColor(Color.red);
				g.setFont(new Font("Ink Free", Font.BOLD, 75));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!"))/2, SCREEN_HEIGTH/2);//centraliza a msg de fim de jogo	
				
				//restart command
				g.setFont(new Font("Ink Free", Font.BOLD, 25));
				g.drawString("Press 'R' to restart the game.", 125, 400);
				
				//score
				g.setColor(Color.red);
				g.setFont(new Font("Ink Free", Font.BOLD, 40));
				FontMetrics metrics2 = getFontMetrics(g.getFont());
				g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());				
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(running) {
					move();
					checkApple();
					checkCollisions();
				}
				repaint();								
			}
			
			public class MyKeyAdapter extends KeyAdapter{
				@Override
				public void keyPressed(KeyEvent e) {
					switch(e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						if(direction != 'R') {
							direction = 'L';
						}
						break;
					case KeyEvent.VK_RIGHT:
						if(direction != 'L') {
							direction = 'R';
						}
						break;
					case KeyEvent.VK_UP:
						if(direction != 'D') {
							direction = 'U';
						}
						break;
					case KeyEvent.VK_DOWN:
						if(direction != 'U') {
							direction = 'D';
						}
						break;							
					}					
				}
			}
			
			
			
			

				
}
	
	


