# CircularRevealButton - V 0.2.1

#### Important note:
Codebase was changed from Java to Kotlin, which means that project new features will be developed, and maintained in Kotlin.

### Library became reachable in JitPack package repository
Add it in your root build.gradle
```allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```dependencies {
	        implementation 'com.github.MGabor11:CircularRevealButton:v0.2.1'
	}
```

## Core functions
This loading button is able to use compression, expansion, and circular reveal animations. If user tap on AnimatedLoadingButton it starts to compress, and it stops, when it's shape seems like a circle. It can stops the process on two ways. First scenario is expanding the button, until it reaches it's original width. Second way is the circular reveal animation, which can be necessary if you want to navigate to a new screen. 

## Usage
### Button attributes
```XML
        <attr name="requiredOffset" format="integer" /> 
        <attr name="progressWidth" format="integer" />
        <attr name="isAnimEnabled" format="boolean" />
        <attr name="circularRevealAnimDuration" format="integer" />
        <attr name="expansionAnimDuration" format="integer"/>
        <attr name="buttonBackgroundColor" format="color" />
        <attr name="progressColor" format="color" />
```

- requiredOffset: Offset of the loading spinner from the edge of the circle in pixels
- progressWidth: Width of the progress indicator in pixels
- isAnimEnabled: Flag for circular reveal animation, in this case button starts circular reveal animation by default on end of progress
- circularRevealAnimDuration: Duration of circular reveal animation in milliseconds
- expansionAnimDuration: Duration of the compression and expansion animation of the loading button in milliseconds
- buttonBackgroundColor: It works same as android:background, it has to be used below API 21
- progressColor: Color of button's progress indicator

### Expansion animation
In this case you have to add AnimatedLoadingButton class to your layout:

![video1](https://user-images.githubusercontent.com/36195029/36480272-00b366ec-170c-11e8-8824-f711fce454de.gif)

```XML
 <com.apppoweron.circularrevealbutton.AnimatedLoadingButton
            android:id="@+id/main_standard_1_btn"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="20dp"
            android:background="@color/test_btn_secondary_color"
            android:text="Standard loading button 1"
            android:textColor="@android:color/white"
            app:progressColor="#F00" />
```

### Circular reveal animation
If you would like to use circular reveal animation with your button, you have to use CircularRevealContainer as a container view:

![video2](https://user-images.githubusercontent.com/36195029/36480276-04023c56-170c-11e8-8e11-ad6f3661075c.gif)

```XML
<?xml version="1.0" encoding="utf-8"?>
<com.apppoweron.circularrevealbutton.container.CircularRevealContainerImpl xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/main_circular_reveal_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

        <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:padding="10dp">
        
                <com.apppoweron.circularrevealbutton.AnimatedLoadingButton
                        android:id="@+id/main_animated_2_btn"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_horizontal"
                        android:layout_marginTop="15dp"
                        android:background="@color/test_btn_color"
                        android:text="Circular reveal button 2"
                        android:textColor="@android:color/holo_red_dark"
                        app:circularRevealAnimDuration="10000"
                        app:isAnimEnabled="true" />
        
         </LinearLayout>
</com.apppoweron.circularrevealbutton.container.CircularRevealContainerImpl>
```
In this code snippet, I deliberately put a LinearLayout between the container and button, because CircularRevealContainer is extending from a FrameLayout. If you would like to use another type of layout arrangement (not FrameLayout), you have to use it like in the example above.

### Key methods
 - To start the compression animation of the button, you don't have to call any method, because it starts automatically on onClick(View view) event.

 - startProgressEndAnimation(...): If you use this one to close the progress animation, the loading button selects the progress end animation. It's decision is based on the isAnimEnabled attribute.
 
 - startCircularReveal(...): In this case, button closes the progress with circular reveal animation. Decision doesn't depend on isAnimEnabled attribute. 

## License
Apache License 2.0

# To be continued ...
