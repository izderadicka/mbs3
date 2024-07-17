FROM ubuntu:24.04

RUN apt-get update && \
    apt-get install -y xdg-utils wget xz-utils python3 libegl1 libopengl0 libxcb-cursor0 libxkbcommon-x11-0

RUN wget -nv -O- https://download.calibre-ebook.com/linux-installer.sh | sh /dev/stdin

ENV LD_LIBRARY_PATH="/opt/calibre/lib"
ENV PATH="/opt/calibre/bin:$PATH"

WORKDIR /ebooks

