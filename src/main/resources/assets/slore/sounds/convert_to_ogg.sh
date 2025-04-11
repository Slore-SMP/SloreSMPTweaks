#!/bin/bash

# Check if ffmpeg is installed
if ! command -v ffmpeg &> /dev/null; then
    echo "ffmpeg is required but not installed. Please install it."
    exit 1
fi

# Create the output directory if it doesn't exist
script_dir="$(dirname "$(realpath "$0")")"
output_dir="$script_dir/output"
mkdir -p "$output_dir"

# Function to convert file to OGG format with Vorbis codec
convert_to_ogg() {
    input_file="$1"
    filename=$(basename "$input_file")
    output_file="$output_dir/${filename%.*}.ogg"

    # Convert to OGG format using the Vorbis codec
    ffmpeg -i "$input_file" -vn -acodec libvorbis -q:a 5 "$output_file"

    if [[ $? -eq 0 ]]; then
        echo "Successfully converted '$input_file' to '$output_file'."
    else
        echo "Failed to convert '$input_file'."
    fi
}

# Loop through all files in the script's directory
for file in "$script_dir"/*; do
    if [[ -f "$file" ]]; then
        # Convert the file if it's .ogg or .mp4
        if [[ "$file" =~ \.ogg$ || "$file" =~ \.mp4$ ]]; then
            convert_to_ogg "$file"
        fi
    fi
done
