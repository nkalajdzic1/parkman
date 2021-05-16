import io
import os
import sys
import base64

from modules.PredictCharacters import predict_license_plate_number
from modules.TrainRecognizeCharacters import train
from modules.DatabaseInteractor import get_transaction_car_photo
from PIL import Image

from definitions import ROOT_DIR

def buildOutput():
    dir_name = ROOT_DIR + '/output'
    
    if not os.path.exists(dir_name):
        os.makedirs(dir_name)
    else:
        filelist = [f for f in os.listdir(dir_name) if f.endswith(".png")]
        for f in filelist:
            os.remove(os.path.join(dir_name, f))    
        
        if not os.path.exists(ROOT_DIR + '/finalized_model.sav'):
            train()

def parsePicture(imageBytes):
    buildOutput()

    Image.open(io.BytesIO(imageBytes)).save("temp.jpg")
    
    plate = predict_license_plate_number("temp.jpg")
    os.remove("temp.jpg")

    return plate[1]

def getBytesFromPath(path):
    with open(sys.argv[2], 'rb') as f:
        return f.read();

print(
    "SUCCESS:" + 
    parsePicture(
        get_transaction_car_photo(
            sys.argv[2],
            sys.argv[3]
        ) if sys.argv[1] == "INSERT" 
        else
            getBytesFromPath(sys.argv[2])
    )
)