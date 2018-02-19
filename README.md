# CircularRevealButton - V 0.1.0

## Core functions
This library is created, because I would like to create an own loading button, which able to use compression, expansion, and circular reveal animations. If user tap on AnimatedLoadingButton it starts to compress, and it stops, when it's shape seems like a circle. It can stops the process on two ways. First scenario is expanding the button, until it reaches it's original width. Second way is the circular reveal animation, which can be necessary if you want to navigate to a new screen. 

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

- requiredOffset: Offset of the loading spinner from the edge of the circle in px
- progressWidth: Width of the progress indicator
- isAnimEnabled: Flag for circular reveal animation, in this case button starts circular reveal animation by default on end of progress
- circularRevealAnimDuration: Duration of circular reveal animation in milliseconds
- expansionAnimDuration: Duration of the compression and expansion animation of the loading button
- buttonBackgroundColor: It works same as android:background, it has to be used under API 21
- progressColor: Color of button's progress indicator

### Expansion animation
In this case you have to add AnimatedLoadingButton class to your layout:
e.g:
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
#### Container attributes

## License
Apache License 2.0
