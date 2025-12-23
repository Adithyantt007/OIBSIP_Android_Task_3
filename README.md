# Java Scientific Calculator for Android

A sleek, functional scientific calculator for Android devices. This app supports standard arithmetic, complex scientific functions, and a toggleable degree/radian system for trigonometric calculations.

## 🚀 Features

- **Standard Operations:** Addition, subtraction, multiplication, and division.
- **Scientific Functions:** Sine, Cosine, Tangent, Logarithms ($log$, $ln$), Square Root, and Factorials ($!$).
- **Smart Formatting:** - Automatically strips unnecessary trailing zeros for a clean display.
  - Limits trigonometric and percentage results to 2 decimal places.
  - Supports high precision ($8$ decimal places) for other scientific results.
- **DEG/RAD Toggle:** Seamlessly switch between Degrees and Radians for trigonometry with a persistent UI indicator.
- **Responsive Layout:** Includes optimized layouts for both **Portrait** and **Landscape** modes.
- **Expression Parsing:** Powered by the `exp4j` library for accurate mathematical evaluation.

## 🛠️ Built With

- **Java** - Primary programming language.
- **Android SDK** - Mobile framework.
- **exp4j** - A library for processing mathematical expressions and custom operators.
- **XML** - For UI design and layouts.

## 🧠 Key Logic: Trig Conversion

The app handles the degree-to-radian conversion internally to ensure compatibility with Java's `Math` library:

```java
if (isDegreeMode) {
    expr = expr.replace("sin(", "sin((pi/180)*");
    // applies to cos and tan as well
}
