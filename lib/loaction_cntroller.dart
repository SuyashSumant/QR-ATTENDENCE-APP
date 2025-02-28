import 'package:get/get.dart';
import 'package:location/location.dart';

class LocationController extends GetxController {
  final RxBool isAccessingLocation = RxBool(false);
  final RxString errorDescription = RxString("");
  final Rx<LocationData?> userLocation = Rx<LocationData?>(null);
  final Location _location = Location();

  Future<void> requestLocationPermission() async {
    isAccessingLocation.value = true;
    try {
      bool serviceEnabled;
      PermissionStatus permissionGranted;

      serviceEnabled = await _location.serviceEnabled();
      if (!serviceEnabled) {
        serviceEnabled = await _location.requestService();
        if (!serviceEnabled) {
          errorDescription.value = "Location services are disabled.";
          isAccessingLocation.value = false;
          return;
        }
      }

      permissionGranted = await _location.hasPermission();
      if (permissionGranted == PermissionStatus.denied) {
        permissionGranted = await _location.requestPermission();
        if (permissionGranted != PermissionStatus.granted) {
          errorDescription.value = "Location permissions are denied.";
          isAccessingLocation.value = false;
          return;
        }
      }

      LocationData locationData = await _location.getLocation();
      userLocation.value = locationData;
      errorDescription.value = "";
    } catch (e) {
      errorDescription.value = "Error getting location: $e";
    } finally {
      isAccessingLocation.value = false;
    }
  }

  void updateIsAccessingLocation(bool b) {
    isAccessingLocation.value = b;
  }

  void updateUserLocation(LocationData data) {
    userLocation.value = data;
  }
}