<h1 align="center">Kreastol Community</h1>

---

## Get started

### Introduction 

This app was made to help the club to develop further more in an informatic way. It was made by Joshua Hegedus.

# The App

## Main Classes

<code>public class <strong>[SpalshScreen](#splashscreen-class)</strong> extends <strong>AppCompatActivity</strong> {}</code>

<p>Responsive for the app start. This class checks if the program is started on the firs time.<br  />If <i>yes</i> then <code>class <strong>OnBoarding</strong></code> will be called, if <i>not</i> the first time then <code>class <strong>UserDashboard</strong></code> will be called.
            </p>

<code>public class <strong>OnBoarding</strong> extends <strong>AppCompatActivity</strong> {}</code>

<p style="margin-top: 10px">This is a preview of the app main funcions.</p>

<code>public class <strong>UserDashboard</strong> extends <strong>AppCompatActivity</strong> {}</code>

The main part of the app is happening here. The menu is set in this class and this class sets up the fragments.

<hr/> 

## Classes Explained

#### <strong>SplashScreen</strong> class: 

The `SplashScreen class` has two main purpose:

- To determine the language of the phone on the first start, and then check it every other start.
- To set the theme if one has set a theme chosen  
- To set the dark or light mode for the user

<hr />

#### <strong>OnBoarding</strong> class:

This class is is for making the app more modern, with is onboarding pages
As for the design  it's made by [Cuberto][cuberto].

[cuberto]: https://github.com/Cuberto/	"Cuberto Profile"