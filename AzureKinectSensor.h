#pragma once
#include <k4a/k4a.h>
#include <opencv2/opencv.hpp>  
#include "RGBDSensor.h"
#include "GlobalAppState.h"
class AzureKinectSensor : public RGBDSensor
{

public:

	AzureKinectSensor();

	~AzureKinectSensor();

	void createFirstConnected();

	bool processDepth();

	bool processColor() {
		return true;
	};

	std::string getSensorName() const {
		return "AzureKinect";
	}

private:

	k4a_capture_t capture = NULL;
	k4a_device_t device = NULL;
};

