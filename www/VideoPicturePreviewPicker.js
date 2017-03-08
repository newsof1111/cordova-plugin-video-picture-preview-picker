

var VideoPicturePreviewPicker = function () {};


VideoPicturePreviewPicker.prototype.openPicker = function (success, fail, options) {
	if (!options) 
		{
			options = {};
		}

	var params = 
		{
			limit_Select: options.limit_Select ? options.limit_Select : 5,
			Is_multiSelect: options.Is_multiSelect ? options.Is_multiSelect : false
		};

	return cordova.exec(success, fail, "VideoPicturePreviewPicker", "openPicker", [params]);
};

VideoPicturePreviewPicker.prototype.init = function () 
{

	var params = {};
	return cordova.exec(null, null, "VideoPicturePreviewPicker", "init", [params]);
};



window.VideoPicturePreviewPicker = new VideoPicturePreviewPicker();
