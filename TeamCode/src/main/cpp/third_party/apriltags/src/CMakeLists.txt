add_library(apriltags
  CameraUtil.cpp
  DebugImage.cpp
  Geometry.cpp 
  GrayModel.cpp
  MathUtil.cpp 
  Refine.cpp
  TagDetector.cpp 
  TagFamily.cpp
  TagFamilies.cpp
  UnionFindSimple.cpp
)

#set(AT_LIBS apriltags ${OPENCV_LDFLAGS})
#
#target_link_libraries(apriltags ${AT_LIBS})
#
#add_executable(camtest camtest.cpp)
#target_link_libraries(camtest ${AT_LIBS})
#
#add_executable(tagtest tagtest.cpp)
#target_link_libraries(tagtest ${AT_LIBS})
#
#add_executable(quadtest quadtest.cpp)
#target_link_libraries(quadtest ${AT_LIBS})