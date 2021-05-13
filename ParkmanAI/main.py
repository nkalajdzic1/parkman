import os
import sys

import time
import calendar;

from PredictCharacters import predict_license_plate_number
from TrainRecognizeCharacters import train
from DatabaseInteractor import new_transaction
from Plate import Plate

def buildOutput():
    dir_name = 'output'
    
    if not os.path.exists(dir_name):
        os.makedirs(dir_name)
    else:
        filelist = [f for f in os.listdir(dir_name) if f.endswith(".png")]
        for f in filelist:
            os.remove(os.path.join(dir_name, f))

    if not os.path.exists('finalized_model.sav'):
        train()

def parsePicture(path):
    buildOutput()

    return predict_license_plate_number(path)

plateString = parsePicture(sys.argv[1])[1]

new_transaction(
    "../ParkmanUI/parkmanDb.sqlite",
    Plate(
        "/home/mula/Desktop/Projects/Python/parkman/ParkmanAI/images/slika4.jpg",
        "/home/mula/Desktop/Projects/Python/parkman/ParkmanAI/images/slika4.jpg",
        plateString,
        calendar.timegm(time.gmtime()),
        None,
        2.3,
        "Emir"
    )
)