#!/bin/bash
docker pull sam9883/tims:intergration-1.0.0
NAME=tims PORT=2000 ./app.run