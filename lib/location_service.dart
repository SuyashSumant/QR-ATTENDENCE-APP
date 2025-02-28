import 'package:get/get.dart';
// import 'package:get/get_core/src/get_main.dart' show Get;
import 'package:location/location.dart';
import 'package:permission_handler/permission_handler.dart' as handler;
import 'loaction_cntroller.dart';

class LocationService{
  LocationService.init();
  static LocationService instance=LocationService.init();

 final Location _location=Location();
  Future<bool> checkForServiceAvailabilty()async{
    bool isEnabled=await _location.serviceEnabled();

    if(isEnabled){
      return Future.value(true);
    }
    isEnabled=await _location.requestService();


    if(isEnabled){
      return Future.value(true);
    }

    return Future.error(false);
  }
  Future<bool> checkForPermission() async{
     PermissionStatus status=await _location.hasPermission();

    if(status==PermissionStatus.denied){
      status =await _location.requestPermission();

      if(status==PermissionStatus.granted){
        return true;
      }
      return false;
    }

    if(status==PermissionStatus.deniedForever){
      Get.snackbar("Permission Needed","We need permission to mark the attendance",
      onTap: (snack){
        handler.openAppSettings();
      })
      .show();
      return false;
    }

    return Future.value(true);
  }

  Future<void> getUserLocation({required LocationController controller}) async{
    if(!( await checkForServiceAvailabilty())){
      controller.errorDescription.value="Service not enabled";
controller.updateIsAccessingLocation((true));
      return;
    }
    if(!(await checkForPermission())){
      controller.errorDescription.value="Permission not given";

      controller.updateIsAccessingLocation((false));

      return;
    }

    final LocationData data=await _location.getLocation();
    controller.updateUserLocation(data);
    controller.updateIsAccessingLocation(false);
  }
}