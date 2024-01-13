#include "stdafx.h"
#include <opencv2/highgui/highgui.hpp>  
#include "AzureKinectSensor.h"
using namespace std;
AzureKinectSensor::AzureKinectSensor() {
	
	const unsigned int colorWidth = 1920;
	const unsigned int colorHeight = 1080;

	const unsigned int depthWidth = 640;
	const unsigned int depthHeight = 576;

	RGBDSensor::init(depthWidth, depthHeight, colorWidth, colorHeight, 2); // color is mapped to depth space
	
}

AzureKinectSensor::~AzureKinectSensor() {

	k4a_device_stop_cameras(device);
	k4a_device_close(device);
}

void AzureKinectSensor::createFirstConnected() {
	uint32_t count = k4a_device_get_installed_count();
	if (count == 0)
	{
		printf("No k4a devices attached!\n");
		return ;
	}

	// Open the first plugged in Kinect device
	if (K4A_FAILED(k4a_device_open(K4A_DEVICE_DEFAULT, &device)))
	{
		printf("Failed to open k4a device!\n");
		return ;
	}

	// Get the size of the serial number
	size_t serial_size = 0;
	k4a_device_get_serialnum(device, NULL, &serial_size);

	// Allocate memory for the serial, then acquire it
	char* serial = (char*)(malloc(serial_size));
	k4a_device_get_serialnum(device, serial, &serial_size);
	printf("Opened device: %s\n", serial);
	free(serial);

	// Configure a stream of 4096x3072 BRGA color data at 15 frames per second
	k4a_device_configuration_t config = K4A_DEVICE_CONFIG_INIT_DISABLE_ALL;
	config.camera_fps = K4A_FRAMES_PER_SECOND_30;
	config.synchronized_images_only = true;
	config.color_format = K4A_IMAGE_FORMAT_COLOR_BGRA32;
	config.color_resolution = K4A_COLOR_RESOLUTION_1080P;//1920 *1080
	config.depth_mode = K4A_DEPTH_MODE_NFOV_UNBINNED;//Depth captured at 640x576.Passive IR is also captured at 640x576.

	// Start the camera with the given configuration
	if (K4A_FAILED(k4a_device_start_cameras(device, &config)))
	{
		printf("Failed to start cameras!\n");
		k4a_device_close(device);
		return ;
	}
	//相机外参
	initializeDepthExtrinsics(mat4f::identity());
	initializeColorExtrinsics(mat4f::identity());
	//相机内参
	k4a_calibration_t calibration;
	k4a_device_get_calibration(device, K4A_DEPTH_MODE_NFOV_UNBINNED, K4A_COLOR_RESOLUTION_1080P, &calibration);
	k4a_calibration_intrinsics_t  color_parameters = calibration.color_camera_calibration.intrinsics;
	k4a_calibration_intrinsics_t  depath_parameters = calibration.depth_camera_calibration.intrinsics;
	initializeDepthIntrinsics(depath_parameters.parameters.param.fx, depath_parameters.parameters.param.fy, depath_parameters.parameters.param.cx, depath_parameters.parameters.param.cy);
	initializeColorIntrinsics(color_parameters.parameters.param.fx, color_parameters.parameters.param.fy, color_parameters.parameters.param.cx, color_parameters.parameters.param.cy);
}
bool AzureKinectSensor::processDepth() {
	if (K4A_WAIT_RESULT_SUCCEEDED != k4a_device_get_capture(device, &capture, K4A_WAIT_INFINITE)) {
		printf("Failed to read a capture\n");
	}

	k4a_image_t depath_image = k4a_capture_get_depth_image(capture);
	if (depath_image != NULL)
	{

		k4a_image_format_t format = k4a_image_get_format(depath_image); // K4A_IMAGE_FORMAT_DEPTH16 

		uint16_t* buffer = (uint16_t*)(void*)k4a_image_get_buffer(depath_image);
		float* depth = getDepthFloat();

		// convert the raw buffer to cv::Mat
		int height = k4a_image_get_height_pixels(depath_image);
		int width = k4a_image_get_width_pixels(depath_image);

		for (int i = 0; i < height*width; ++i) {
				float dF = (float)buffer[i] * 0.001f;
				if (dF !=0)
					depth[i] = dF;
				else
					depth[i] = -std::numeric_limits<float>::infinity();
		}

		k4a_image_release(depath_image);
	}

	k4a_image_t color_image = k4a_capture_get_color_image(capture);
	if (color_image != NULL)
	{

		k4a_image_format_t format = k4a_image_get_format(color_image); // K4A_IMAGE_FORMAT_DEPTH16 

		// get raw buffer
		uint8_t* buffer = k4a_image_get_buffer(color_image);
		// convert the raw buffer to cv::Mat
		int height = k4a_image_get_height_pixels(color_image);
		int width = k4a_image_get_width_pixels(color_image);

		cv::Mat cv_color(height, width, CV_8UC4, buffer, k4a_image_get_stride_bytes(color_image));
	
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				cv::Vec4b vc = cv_color.at<cv::Vec4b>(i, j);
				unsigned int c = 0;
				c |= vc[0];
				c <<= 8;
				c |= vc[1];
				c <<= 8;
				c |= vc[2];
				c |= 0xFF000000;
				//cout << vc << endl;
				((LONG*)m_colorRGBX)[i* width +j] =c;
				//std::cout << (int)m_colorRGBX[i * width + j][0]<<"  " << (int)m_colorRGBX[i * width + j][1] <<" " << (int)m_colorRGBX[i * width + j][2]<<" " << (int)m_colorRGBX[i * width + j][3] << std::endl;
			}
		}

	/*	for (int i = 0; i < height * width; ++i) {
			m_colorRGBX[i ] = vec4uc(buffer[i*4], buffer[i*4+1], buffer[i*4+2], buffer[i*4+3]);
		}*/
		k4a_image_release(color_image);
	}

	k4a_capture_release(capture);
	return true;
}