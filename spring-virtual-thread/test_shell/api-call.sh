
for var in {1..100}
do
  curl -X GET --location "http://localhost:8080/virtual" &
done
