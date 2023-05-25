package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;


import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manstate=0;
	int pause=0;
	float gravity=0.2f;
	float velocity=0;
	int manY=0;
	int score=0;


	ArrayList<Integer>coinx=new ArrayList<>();
	ArrayList<Integer>coiny= new ArrayList<>();

	ArrayList<Integer>bombx=new ArrayList<>();
	ArrayList<Integer>bomby= new ArrayList<>();
	ArrayList<Rectangle>coinrect= new ArrayList<>();
	ArrayList<Rectangle>bombrect= new ArrayList<>();
	Texture coin;
	int coincount;
	int bombcount;
	Texture bomb;
	Rectangle manrect;
	BitmapFont font;
	int gamestate=0;
	Texture dizzy;



	Random random;



	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background= new Texture("bg.png");
		man= new Texture[4];
		man[0]= new Texture("frame-1.png");
		man[1]= new Texture("frame-2.png");
		man[2]= new Texture("frame-3.png");
		man[3]= new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight()/2;

		coin=new Texture("coin.png");
		bomb= new Texture("bomb.png");

		random = new Random();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		dizzy=new Texture("dizzy-1.png");




	}

	public void makecoin()
	{
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coiny.add((int)height);
		coinx.add(Gdx.graphics.getWidth());
	}
	public void makebomb()
	{
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bomby.add((int)height);
		bombx.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gamestate == 1) {




			//coins
			if(coincount<100)
			{
				coincount++;

			}else{
				coincount=0;

				makecoin();
			}
			coinrect.clear();
			for (int i = 0; i <coinx.size() ; i++) {
				batch.draw(coin, coinx.get(i),coiny.get(i));
				coinx.set(i,coinx.get(i)-8);
				coinrect.add(new Rectangle(coinx.get(i), coiny.get(i),coin.getWidth(),coin.getHeight()));

			}
			//bombs
			if(bombcount<250)
			{
				bombcount++;

			}else{
				bombcount=0;

				makebomb();
			}
			bombrect.clear();
			for (int i = 0; i <bombx.size() ; i++) {
				batch.draw(bomb, bombx.get(i),bomby.get(i));
				bombx.set(i,bombx.get(i)-4);
				bombrect.add(new Rectangle(bombx.get(i), bomby.get(i),bomb.getWidth(),bomb.getHeight()));

			}

			if(Gdx.input.justTouched())
			{
				velocity=-10;
			}



			if(pause<5)
			{
				pause++;
			}else{
				pause=0;
				if(manstate<3)
				{
					manstate++;
				}else{
					manstate=0;
				}
			}

			velocity+=gravity;
			manY-=velocity;

			if(manY<=0)
			{
				manY=0;
			}


			//game is live

		}else if(gamestate==0){
			font.draw(batch,"click to start",500,200);
			//game start
			if(Gdx.input.justTouched())
			{
				gamestate=1;
			}
		}else{
			//game over

			if(Gdx.input.justTouched())
			{
				gamestate=1;
				manY=Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				coinx.clear();
				coiny.clear();
				coinrect.clear();
				coincount=0;
				bombx.clear();
				bomby.clear();
				bombrect.clear();
				bombcount=0;
			}

		}
		if(gamestate==2)
		{
			batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY );
			font.draw(batch,"click to start again",300,200);
		}else{
			batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);

		}



	manrect= new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2, manY,man[manstate].getWidth(),man[manstate].getHeight());

	for(int i=0; i<coinrect.size(); i++)
	{
		if(Intersector.overlaps(manrect,coinrect.get(i)))
		{
			Gdx.app.log("coin","collision!");
			score++;
			coinrect.remove(i);
			coinx.remove(i);
			coiny.remove(i);
			break;
		}
	}
		for(int i=0; i<bombrect.size(); i++)
		{
			if(Intersector.overlaps(manrect,bombrect.get(i)))
			{
				Gdx.app.log("bomb","collision!");
				gamestate=2;
			}
		}

		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
