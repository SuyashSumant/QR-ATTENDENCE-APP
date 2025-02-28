import 'package:flutter/foundation.dart';
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
      home: AuthCheck(), // Check authentication status
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

  bool _isFirstLaunch = false; // Flag to track first launch

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
  @override
  Widget build(BuildContext context) {


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
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Welcome")),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Center(
            child: Text(
              "Welcome to QR Attendance App",
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
          ),
          SizedBox(height: 20),
          Align(
            alignment: Alignment.centerRight,
            child: Padding(
              padding: const EdgeInsets.only(right: 16.0),
              child: ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => LoginPage()),
                  );
                },
                child: Text("Go to Login"),
              ),
            ),
          ),
        ],
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
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextField(
              controller: emailController,
              decoration: InputDecoration(labelText: "Email", border: OutlineInputBorder()),
              keyboardType: TextInputType.emailAddress,
            ),
            SizedBox(height: 10),
            TextField(
              controller: passwordController,
              decoration: InputDecoration(labelText: "Password", border: OutlineInputBorder()),
              obscureText: true,
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () async {
                User? user = await _authService.signInWithEmailAndPassword(
                  emailController.text.trim(),
                  passwordController.text.trim(),
                );
                if (user != null) {
                  Get.find<LocationController>().requestLocationPermission().then((_){
                 if(Get.find<LocationController>().userLocation.value!=null){
                   Navigator.pushReplacement(
                     context,
                     MaterialPageRoute(builder: (context) => HomeScreen(employeeName: user.email ?? "User")),
                   );
                 }else{
                   ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Location permission required.")));
                   Get.find<FirebaseAuthService>().signOut(); // Sign out the user
                   Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => LoginPage()));
                 }
                      });

                }
                else {
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Login Failed")));
                }
              },
              child: Text("Login"),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) => SignUpPage()));
              },
              child: Text("Create Account"),
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
                QrImageView(
                  data: this.employeeName,
                  version: QrVersions.auto,
                  size: 320,
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
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextField(
              controller: nameController,
              decoration: InputDecoration(labelText: "Name", border: OutlineInputBorder()),
              keyboardType: TextInputType.emailAddress,
            ),
            SizedBox(height: 10),
            TextField(
              controller: emailController,
              decoration: InputDecoration(labelText: "Email", border: OutlineInputBorder()),
              keyboardType: TextInputType.emailAddress,
            ),
            SizedBox(height: 10),
            TextField(
              controller: passwordController,
              decoration: InputDecoration(labelText: "Password", border: OutlineInputBorder()),
              obscureText: true,
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () async {
                User? user = await _authService.signUpWithEmailAndPassword(
                  emailController.text.trim(),
                  passwordController.text.trim(),
                );
                if (user != null) {
                  Get.find<LocationController>().requestLocationPermission().then((_){
                    if(Get.find<LocationController>().userLocation.value!=null){
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(builder: (context) => HomeScreen(employeeName: user.email ?? "User")),
                      );
                    }
                    else{
                      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Location permission required.")));
                      Get.find<FirebaseAuthService>().signOut(); // Sign out the user
                      Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => LoginPage()));
                    }
                  });

                } else {
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text("Sign Up Failed")));
                }
              },
              child: Text("Sign Up"),
            ),
          ],
        ),
      ),
    );
  }
}
