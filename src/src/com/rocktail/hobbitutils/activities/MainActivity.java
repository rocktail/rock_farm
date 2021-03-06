package com.rocktail.hobbitutils.activities;

import com.rocktail.hobbitutils.R;
import com.rocktail.hobbitutils.controllers.TroopsTrainingResultPresenter;
import com.rocktail.hobbitutils.controllers.TroopsTrainingResourcesPresenter;
import com.rocktail.hobbitutilst.models.TroopsTrainingCalculationResult;

import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity 
	implements IMainActivity, OnBackStackChangedListener {

    private static final int RESULT_SETTINGS = 0;
    private long _foodResource;
    private long _woodResource;
    private long _stoneResource;
    private long _oreResource;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading default preferences values with unit costs
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        TroopsTrainingResourcesFragment inputFragment = new TroopsTrainingResourcesFragment();
        TroopsTrainingResourcesPresenter presenter = new TroopsTrainingResourcesPresenter(this, inputFragment);
        inputFragment.setPresenter(presenter);
        
        fragmentTransaction.add(R.id.pager, inputFragment, "Resources");
        fragmentTransaction.commit(); 
        
        fragmentManager.addOnBackStackChangedListener(this);
    }

	/* (non-Javadoc)
	 * @see com.rocktail.hobbitutils.activities.IMainActivity#createResultFragment(com.rocktail.hobbitutilst.models.TroopsTrainingCalculationResult)
	 */
	@Override
	public void createResultFragment(TroopsTrainingCalculationResult res) {
		FragmentManager fragmentManager = getFragmentManager();
		
		TroopsTrainingResourcesFragment currFrag = 
    			(TroopsTrainingResourcesFragment)fragmentManager.findFragmentByTag("Resources");
		
		//saving resources amount to put them back when user returns from result fragment
		this._foodResource = currFrag.getFoodResource();
		this._woodResource = currFrag.getWoodResource();
		this._stoneResource = currFrag.getStoneResource();
		this._oreResource = currFrag.getOreResource();
		
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		TroopsTrainingResultFragment resultFragment = new TroopsTrainingResultFragment();

		TroopsTrainingResultPresenter presenter = new TroopsTrainingResultPresenter(resultFragment, res);
		resultFragment.setPresenter(presenter);
		
		fragmentTransaction.replace(R.id.pager, resultFragment);
		fragmentTransaction.addToBackStack(null);

		fragmentTransaction.commit();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.action_settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;
 
        }
 
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	if (this.getFragmentManager().getBackStackEntryCount() > 0) {
    		this.getFragmentManager().popBackStack();
    	}
    	else {
    		super.onBackPressed();
    	}
    		
    }

	@Override
	public void onBackStackChanged() {
		FragmentManager manager = getFragmentManager();

        if (manager != null)
        {
        	TroopsTrainingResourcesFragment currFrag = 
        			(TroopsTrainingResourcesFragment)manager.findFragmentByTag("Resources");

            currFrag.setFoodResource(this._foodResource);
            currFrag.setWoodResource(this._woodResource);
            currFrag.setStoneResource(this._stoneResource);
            currFrag.setOreResource(this._oreResource);
        } 
		
	}
}
