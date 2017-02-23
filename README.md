# android-things-button-examples
This repository will demonstrate the different ways in which you can access and deal with physical buttons on Android Things. 

There are 4 different ways in which you can use/access a button in Android Things:

## Simple Peripheral I/O
Consuming the Peripheral IO, we can get raw access to the GPIO pin which will receive the value 
if the button is pressed or not.


Checkout branch: https://github.com/riggaroo/android-things-button-examples/tree/simple-peripheral-io


## Button Input Driver
You can create your own custom Input driver combined with a GPIO pin, that will trigger normal key events.
On this branch, the KEYCODE_SPACE is triggered when the button is pressed.
This is a basic naive approach example of writing a custom user driver.


Checkout branch: https://github.com/riggaroo/android-things-button-examples/tree/button-input-driver

## Button User Driver
This is based off the contrib-driver libraries given in the android-things repository. 
It triggers a KEYCODE_SPACE but the driver itself is not written here. It comes from the library.


Checkout branch: https://github.com/riggaroo/android-things-button-examples/tree/button-user-driver

## Master - Button Library Callback - No KEYCODE events
On master branch, the android things contrib-driver is used, but we do not use the onKeyEvent method, 
we use the callback defined on the Button.

Checkout branch: https://github.com/riggaroo/android-things-button-examples/