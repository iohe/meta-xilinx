DESCRIPTION = "ARM Trusted Firmware"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://license.md;md5=829bdeb34c1d9044f393d5a16c068371"

PROVIDES = "virtual/atf"

inherit deploy

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

BRANCH = "master"
SRC_URI = "git://github.com/Xilinx/arm-trusted-firmware.git;protocol=https;branch=${BRANCH}"

# This points at the 'xilinx-v2016.3' tag
SRCREV ?= "a9e3716615a23c78e3cdea5b5b2f840f89817cb1"

PV = "1.2+xilinx+git${SRCPV}"

COMPATIBLE_MACHINE = "zynqmp"
PLATFORM_zynqmp = "zynqmp"

# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure() {
	:
}

do_compile() {
	oe_runmake -C ${S} BUILD_BASE=${B} PLAT=${PLATFORM} RESET_TO_BL31=1 bl31
}

do_install() {
	:
}

do_deploy() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${B}/${PLATFORM}/release/bl31/bl31.elf ${DEPLOYDIR}/arm-trusted-firmware-${PACKAGE_ARCH}.elf
	install -m 0644 ${B}/${PLATFORM}/release/bl31.bin ${DEPLOYDIR}/arm-trusted-firmware-${PACKAGE_ARCH}.bin
}
addtask deploy before do_build after do_compile
