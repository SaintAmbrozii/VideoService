Simple WebFlux Video Service with Upload files to resource and saving to Postgress database.
Video File reading with absolute path to notmaize in databse and response with URiResource

Method getAllVideos get Flux<VideoFileDTO> with resource Path to streaming GET: http://localhost:8080/api/videos

Method getById get fileById GET:{id} http://localhost:8080/api/videos/get/6

Method getResourceByName streaming video to URIResource GET:{title} http://localhost:8080/api/videos/inna-bad-boys_326792.mp4

Method upload upload file to resource with description and title and saving to Postgress R2DBC POST: http://localhost:8080/api/videos

Method updated uptated title and description to databdase PATCH:{id} http://localhost:8080/api/videos

Method delete deleted file to database and fileResource DELETE:{id} http://localhost:8080/api/videos


![postman-inna-2](https://github.com/SaintAmbrozii/VideoService/assets/125075635/46120fd1-6d30-451a-98ca-66f11fe38b13)
