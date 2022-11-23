#!/bin/bash

# Convert an image to a specific overlay, and add a watermark while doing this.
# Input needs to be a 1024x1024 PNG image

usage() {
  echo "Usage:"
  echo "   watermark.sh -i [input] -o [outputDir] -t text -c color -a -m"
  echo "   -i, -o are mandatory arguments."
  echo "   -a AND/OR -m needs to be provided so the script knows which output to generate."
  echo ""
  echo "-f | --file       The input file (a 1024x1024 PNG file)"
  echo "-o | --output     Output directory"
  echo "-t | --text       Text that will be added to the bottom of the image"
  echo "-c | --color      Color for the box on the icon. Provide the hexcolor (#ffffff) or any of the predefined options: red, yellow, blue, green and black."
  echo "-a | --android    Flag to process output for Android"
  echo "-i | --ios        Flag to process output for iOS"
  exit 1
}

if ! command -v magick &> /dev/null; then
    echo "Please install Imagick first"
    exit 1
fi

FOR_IOS=0
FOR_ANDROID=0
TEXT="debug"
FILL_COLOR="#0008"

while [ $# -gt 0 ]; do
  case $1 in
    -f|--file)
      INPUT_FILE="$2"
      shift 2
      ;;
    -o|--output)
      OUTPUT_DIR="$2"
      shift 2
      ;;
    -t|--text)
      TEXT="$2"
      shift 2
      ;;
    -c|--color)
      case $2 in
      red)
        FILL_COLOR="#ff0000"
        ;;
      yellow)
        FILL_COLOR="#ffff00"
        ;;
      blue)
        FILL_COLOR="#00ffff"
        ;;
      green)
        FILL_COLOR="#009933"
        ;;
      black)
        FILL_COLOR="#000000"
        ;;
      *)
        FILL_COLOR="$2"
        ;;
      esac
      shift 2
      ;;
    -a|--android|--Android|--ANDROID)
      # Enable Android output
      FOR_ANDROID=1
      shift
      ;;
    -i|--ios|--iOS|--IOS)
      # Enable iOS output
      FOR_IOS=1
      shift
      ;;
    -*)
      #Unknown option
      echo "Unknown option $1"
      usage
      exit 1
      ;;
    *)
      #Positional param
      echo "$@"
      echo "No positional params allowed"
      usage
      ;;
  esac
done

echo "Processing icon for $INPUT_FILE color: $FILL_COLOR and text: $TEXT"

if [ -z "$INPUT_FILE" ]; then
  echo "No input file"
  usage
elif [ -z  "$OUTPUT_DIR" ]; then
  echo "No output directory"
  usage
elif [ "$((FOR_ANDROID + FOR_IOS))" = 0 ]; then
  echo "Provide either android (-a | --android) or ios (-m | --ios) flag"
  usage
fi

WIDTH=$(identify $INPUT_FILE | cut -d\  -f3 | cut -dx -f2)
RECT_HEIGHT=$(($WIDTH / 5))
RECT_START=$(($RECT_HEIGHT*4))
FONT_SIZE=$(($WIDTH / 6))

mkdir -p $OUTPUT_DIR

# First create a temporary file:
echo "Adding box overlay to original icon ..."
magick convert "$INPUT_FILE" \
    -fill "$FILL_COLOR" \
    -draw "rectangle 0,${RECT_START},${WIDTH},${WIDTH}" \
    -gravity south \
    -style oblique \
    "${OUTPUT_DIR}/tmp-box.png"

# First create a temporary file:
echo "Adding Text $TEXT to boxed icon ..."
magick convert "${OUTPUT_DIR}/tmp-box.png" \
    -gravity south \
    -fill white \
    -stroke "#333333" \
    -stretch ExtraExpanded \
    -strokewidth 3 \
    -font "Systemskrift-ExtraExpanded-Black" \
    -style oblique \
    -pointsize $FONT_SIZE \
    -draw "text 0,0 $TEXT" \
    "${OUTPUT_DIR}/tmp-txt.png"

# Then resize this file to all sizes depending on platform:
BASENAME=$(basename "$INPUT_FILE")
EXTENSION="${BASENAME##*.}"
NAME_WITHOUT_EXTENSION="${BASENAME%.*}"

mkdir -p $OUTPUT_DIR

if [ $FOR_IOS -ne 0 ]; then
    SIZES="20 29 40 58 60 76 80 87 120 152 167 180 1024"
    IOS_OUTPUT_DIR="${OUTPUT_DIR}/ios"
    mkdir -p "${IOS_OUTPUT_DIR}"
    for SIZE in ${SIZES}; do
        echo "Generating scaled icon @ ${SIZE} ..."
        magick convert "${OUTPUT_DIR}/tmp-txt.png" \
            -resize "${SIZE}x${SIZE}" \
            "${IOS_OUTPUT_DIR}/${NAME_WITHOUT_EXTENSION}@${SIZE}.${EXTENSION}"
    done
fi

if [ $FOR_ANDROID  -ne 0 ]; then
    CONFIGS="36.ldpi 48.mdpi 72.hdpi 96.xhdpi 144.xxhdpi 192.xxxhdpi"
    ANDROID_OUTPUT_DIR="$OUTPUT_DIR"
    for CONFIG in ${CONFIGS}; do
        SIZE="${CONFIG%.*}"
        RES="${CONFIG##*.}"
        CONFIG_OUTPUT_DIR="$ANDROID_OUTPUT_DIR/mipmap-$RES"
        echo "Generating scaled icon @ ${SIZE} and resolution $RES ..."
        mkdir -p "$CONFIG_OUTPUT_DIR"
        magick convert "${OUTPUT_DIR}/tmp-txt.png" \
            -resize "${SIZE}x${SIZE}" \
            "${ANDROID_OUTPUT_DIR}/mipmap-${RES}/${NAME_WITHOUT_EXTENSION}.${EXTENSION}"
    done
fi

rm "${OUTPUT_DIR}/tmp-box.png"
rm "${OUTPUT_DIR}/tmp-txt.png"

echo "All done!"
