import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'firebase_auth.dart'; // Import Firebase Authentication Service
import 'package:firebase_auth/firebase_auth.dart';
import 'package:get/get.dart'; // Import GetX
import 'loaction_cntroller.dart'; // Import your LocationController
import 'package:shared_preferences/shared_preferences.dart';

void main() async {

  WidgetsFlutterBinding.ensureInitialized();

  await Firebase.initializeApp();

  Get.put(LocationController());

  runApp(MyApp());

}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: WelcomePage(), // Check authentication status
    );
  }
}

class AuthCheck extends StatefulWidget { //change to stateful
  @override
  _AuthCheckState createState() => _AuthCheckState();
}
// Check if the user is logged in or not
// Check if the user is logged in or not
class _AuthCheckState extends State<AuthCheck> {
  final FirebaseAuthService _authService = FirebaseAuthService();
  final LocationController locationController = Get.find<LocationController>(); // Get the controller
  bool _isFirstLaunch = true; // Flag to track first launch
  @override

  void initState() {
    super.initState();
    _checkFirstLaunch();
  }

  Future<void> _checkFirstLaunch() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      _isFirstLaunch = prefs.getBool('firstLaunch') ?? true;
    });

    if (_isFirstLaunch) {
      await prefs.setBool('firstLaunch', false); // Set to false after first launch
    }
  }

  Widget build(BuildContext context) {
    if (_isFirstLaunch) {
      return WelcomePage(); // Show WelcomePage on first launch
    }
    return StreamBuilder<User?>(
      stream: _authService.authStateChanges,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.active) {
          final user = snapshot.data;
          if (user == null) {
            return LoginPage();
          } else {
// Request location permission after login
            WidgetsBinding.instance.addPostFrameCallback((_) {
              locationController.requestLocationPermission().then((_) {
                if (locationController.userLocation.value != null) {
                  Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(builder: (context) => HomeScreen(employeeName: user.email ?? "User")),
                  );
                } else {
// Handle permission denial or location error
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Location permission required.")));
                  _authService.signOut(); // Sign out the user
                  Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => LoginPage())); // Return to login
                }
              });
            });
            return Scaffold(
              body: Center(child: CircularProgressIndicator()), // Show loading
            );
          }
        }
        return Center(child: CircularProgressIndicator()); // Loading indicator
      },
    );
  }
}

class WelcomePage extends StatelessWidget {
  Widget build(BuildContext context){
    return Material(
      child: Container(
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        child: Stack(
          children: [
            Stack(
              children: [
                Container(
                  width:  MediaQuery.of(context).size.width,
                  height: MediaQuery.of(context).size.height/1.6,
                  decoration: BoxDecoration(
                    color: Colors.white,
                  ),
                ),
                Container(
                  width:  MediaQuery.of(context).size.width,
                  height: MediaQuery.of(context).size.height/1.6,
                  decoration: BoxDecoration(
                    color: Colors.black,
                    borderRadius: BorderRadius.only(bottomRight: Radius.circular(70)),
                  ),

                  child: Center(
                    child: Icon(
                      Icons.account_circle,
                      size: 300,
                      color: Colors.white,
                    ),
                  ),
                ),
              ],
            ),
            Align(
              alignment: Alignment.bottomCenter,
              child: Container(
                width:  MediaQuery.of(context).size.width,
                height: MediaQuery.of(context).size.height/2.666,
                decoration: BoxDecoration(
                  color: Colors.black,
                ),
              ),
            ),
            Align(
              alignment: Alignment.bottomCenter,
              child: Container(
                width:  MediaQuery.of(context).size.width,
                height: MediaQuery.of(context).size.height/2.666,
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.only(topLeft: Radius.circular(70)
                  ),
                ),



                child: Column(
                  children:[
                    SizedBox(height: 30),
                    Text("Mark your attendance ",
                      style:TextStyle(
                        fontSize: 23,
                        fontWeight: FontWeight.w500,
                        letterSpacing: 1,
                        wordSpacing: 2,
                      ),
                    ),
                    SizedBox(height: 15),
                    Padding(padding:EdgeInsets.symmetric(horizontal: 40),
                      child: Text("QR code attendance makes it easy. Just scan and "
                          "mark your attendance."
                        ,
                        textAlign: TextAlign.center,
                        style: TextStyle(
                            fontSize: 16,
                            color: Colors.black.withOpacity(0.6)
                        ),
                      ),
                    ),
                    SizedBox(height: 45),
                    Material(
                      color: Colors.black,
                      borderRadius: BorderRadius.circular(10),
                      child: InkWell(
                        onTap: (){
                          Navigator.push(context,
                          MaterialPageRoute(builder: (context)=>LoginPage()));
                        },
                        child: Container(
                          padding: EdgeInsets.symmetric(
                              vertical: 15,horizontal: 80
                          ),
                          child: Text("Let's start ",
                            style: TextStyle(
                              color: Colors.white,
                              fontSize: 22,
                              fontWeight: FontWeight.bold,
                              letterSpacing: 1,
                            ),),
                        ),
                      ),
                    ),
                  ],
                ),
              ),


            ),
          ],
        ),
      ),
    );
  }
}
// Login Page
class LoginPage extends StatelessWidget {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final FirebaseAuthService _authService = FirebaseAuthService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Login")),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(height: 80),
            Icon(
              Icons.lock,
              size: 100,
              color: Colors.black,
            ),
            SizedBox(height: 100),
            Text(
              "Welcome back!",
              style: TextStyle(
                color: Colors.black,
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 20),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  TextField(
                    controller: emailController,
                    decoration: InputDecoration(
                      labelText: "Email",
                      border: OutlineInputBorder(),
                    ),
                    keyboardType: TextInputType.emailAddress,
                  ),
                  SizedBox(height: 10),
                  TextField(
                    controller: passwordController,
                    decoration: InputDecoration(
                      labelText: "Password",
                      border: OutlineInputBorder(),
                    ),
                    obscureText: true,
                  ),
                  SizedBox(height: 50),

                  // Login Button with Full Width
                  SizedBox(
                    width: double.infinity, // Makes button width same as input fields
                    height: 50, // Optional: Increase button height
                    child: ElevatedButton(
                      onPressed: () async {
                        User? user = await _authService.signInWithEmailAndPassword(
                          emailController.text.trim(),
                          passwordController.text.trim(),
                        );
                        if (user != null) {
                          Get.find<LocationController>().requestLocationPermission().then((_) {
                            if (Get.find<LocationController>().userLocation.value != null) {
                              Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(builder: (context) => HomeScreen(employeeName: user.email ?? "User")),
                              );
                            } else {
                              ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(content: Text("Location permission required.")),
                              );
                              Get.find<FirebaseAuthService>().signOut();
                              Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(builder: (context) => LoginPage()),
                              );
                            }
                          });
                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text("Login Failed")),
                          );
                        }
                      },
                      style: ElevatedButton.styleFrom(
                          backgroundColor:Colors.black,
                        foregroundColor: Colors.white,

                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12.5),
                )
                      ),
                      child: Text("Login",
                          style: TextStyle(
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                          fontSize: 16,
                            letterSpacing: 1

                      ),),
                    ),
                  ),
                  SizedBox(height: 20),

                  // Create Account Button with Full Width
                  SizedBox(
                    width: double.infinity,
                    height: 50,
                    child: ElevatedButton(
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => SignUpPage()),
                        );
                      },
                      style: ElevatedButton.styleFrom(
                          backgroundColor:Colors.black,
                          foregroundColor: Colors.white,
                          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12.5),)
                      ),
                      child: Text("Create Account",
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        fontSize: 16,
                          letterSpacing: 1


                      ),),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// Home Screen
class HomeScreen extends StatelessWidget {
  final String employeeName;
  final FirebaseAuthService _authService = FirebaseAuthService();
  HomeScreen({required this.employeeName});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(employeeName),
        actions: [
          IconButton(
            icon: Icon(Icons.logout),
            onPressed: () async {
              await _authService.signOut();
              Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => LoginPage()));
            },
          ),
        ],
      ),
      body: Center(
          child:Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,

              children:[
                Container(
                  decoration:BoxDecoration(
                  color: Colors.grey[200], // Background color
                  borderRadius: BorderRadius.circular(12.0), // Rounded corners
                  border: Border.all(color: Colors.blue, width: 2), // Border
          ),
                  padding: const EdgeInsets.all(16.0),
                child:  QrImageView(
                    data: this.employeeName,
                    version: QrVersions.auto,
                    size: 320,
                  ),
                ),

                const SizedBox(height: 20.0,),
                const Text('Scan this QR code'),
              ]
          )
      ),
    );
  }
}
// Sign Up Page
class SignUpPage extends StatelessWidget {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController nameController = TextEditingController();
  final FirebaseAuthService _authService = FirebaseAuthService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Sign Up")),
      body: SingleChildScrollView(
        child: Column(
          children: [
            SizedBox(height: 80), // Space below AppBar
            Icon(
              Icons.lock,
              size: 100,
              color: Colors.black, // Optional color
            ),
            SizedBox(height: 100),
            Text(
              "Enter the details to create the account!",
              style: TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 20),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  TextField(
                    controller: nameController,
                    decoration: InputDecoration(
                      labelText: "Name",
                      border: OutlineInputBorder(),
                    ),
                  ),
                  SizedBox(height: 10),
                  TextField(
                    controller: emailController,
                    decoration: InputDecoration(
                      labelText: "Email",
                      border: OutlineInputBorder(),
                    ),
                    keyboardType: TextInputType.emailAddress,
                  ),
                  SizedBox(height: 10),
                  TextField(
                    controller: passwordController,
                    decoration: InputDecoration(
                      labelText: "Password",
                      border: OutlineInputBorder(),
                    ),
                    obscureText: true,
                  ),
                  SizedBox(height: 50),

                  // Sign Up Button
                  SizedBox(
                    width: double.infinity,
                    height: 50,
                    child: ElevatedButton(
                      onPressed: () async {
                        User? user = await _authService.signUpWithEmailAndPassword(
                          emailController.text.trim(),
                          passwordController.text.trim(),
                        );
                        if (user != null) {
                          Get.find<LocationController>().requestLocationPermission().then((_) {
                            if (Get.find<LocationController>().userLocation.value != null) {
                              Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(
                                  builder: (context) =>
                                      HomeScreen(employeeName: user.email ?? "User"),
                                ),
                              );
                            } else {
                              ScaffoldMessenger.of(context).showSnackBar(
                                SnackBar(content: Text("Location permission required.")),
                              );
                              Get.find<FirebaseAuthService>().signOut();
                              Navigator.pushReplacement(
                                context,
                                MaterialPageRoute(builder: (context) => LoginPage()),
                              );
                            }
                          });
                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text("Sign Up Failed")),
                          );
                        }
                      },
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.black,
                        foregroundColor: Colors.white,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12.5),
                        ),
                      ),
                      child: Text(
                        "Sign Up",
                        style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                    ),
                  ),
                  SizedBox(height: 20),

                  // Go to Login Button
                  SizedBox(
                    width: double.infinity,
                    height: 50,
                    child: ElevatedButton(
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => LoginPage()),
                        );
                      },
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.black,
                        foregroundColor: Colors.white,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(12.5),
                        ),
                      ),
                      child: Text(
                        "Go to Login",
                        style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold,letterSpacing: 1
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

