Summary: lark
Name: lark
Version: 1.1.0
Release: 1
License: Unknown
Vendor: Unknown

%if "x" != "x"
URL: 
%endif

%if "x/opt" != "x"
Prefix: /opt
%endif

Provides: lark

%if "x" != "x"
Group: 
%endif

Autoprov: 0
Autoreq: 0
%if "x" != "x" || "x" != "x"
Requires:  
%endif

#comment line below to enable effective jar compression
#it could easily get your package size from 40 to 15Mb but
#build time will substantially increase and it may require unpack200/system java to install
%define __jar_repack %{nil}

# on RHEL we got unwanted improved debugging enhancements
%define _build_id_links none

%define package_filelist %{_builddir}/%{name}.files
%define app_filelist %{_builddir}/%{name}.app.files
%define filesystem_filelist %{_builddir}/%{name}.filesystem.files

%define default_filesystem / /opt /usr /usr/bin /usr/lib /usr/local /usr/local/bin /usr/local/lib

%description
lark

%global __os_install_post %{nil}

%prep

%build

%install
rm -rf %{buildroot}
install -d -m 755 %{buildroot}/opt/lark
cp -r %{_sourcedir}/opt/lark/* %{buildroot}/opt/lark
if [ "$(echo %{_sourcedir}/lib/systemd/system/*.service)" != '%{_sourcedir}/lib/systemd/system/*.service' ]; then
  install -d -m 755 %{buildroot}/lib/systemd/system
  cp %{_sourcedir}/lib/systemd/system/*.service %{buildroot}/lib/systemd/system
fi
%if "x" != "x"
  %define license_install_file %{_defaultlicensedir}/%{name}-%{version}/%{basename:}
  install -d -m 755 "%{buildroot}%{dirname:%{license_install_file}}"
  install -m 644 "" "%{buildroot}%{license_install_file}"
%endif
(cd %{buildroot} && find . -path ./lib/systemd -prune -o -type d -print) | sed -e 's/^\.//' -e '/^$/d' | sort > %{app_filelist}
{ rpm -ql filesystem || echo %{default_filesystem}; } | sort > %{filesystem_filelist}
comm -23 %{app_filelist} %{filesystem_filelist} > %{package_filelist}
sed -i -e 's/.*/%dir "&"/' %{package_filelist}
(cd %{buildroot} && find . -not -type d) | sed -e 's/^\.//' -e 's/.*/"&"/' >> %{package_filelist}
%if "x" != "x"
  sed -i -e 's|"%{license_install_file}"||' -e '/^$/d' %{package_filelist}
%endif

%files -f %{package_filelist}
%if "x" != "x"
  %license "%{license_install_file}"
%endif

%post
package_type=rpm

# Install udev rules
install -m 644 /opt/lark/lib/runtime/conf/udev/*.rules /etc/udev/rules.d

# Reload udev rules
udevadm control --reload

# Optionally trigger udev rules
udevadm trigger

# Make sure the plugdev group exists
groupadd -f plugdev

# Make sure the current user is added to plugdev
usermod -aG plugdev `whoami`


%pre
package_type=rpm
file_belongs_to_single_package ()
{
  if [ ! -e "$1" ]; then
    false
  elif [ "$package_type" = rpm ]; then
    test `rpm -q --whatprovides "$1" | wc -l` = 1
  elif [ "$package_type" = deb ]; then
    test `dpkg -S "$1" | wc -l` = 1
  else
    exit 1
  fi
}


do_if_file_belongs_to_single_package ()
{
  local file="$1"
  shift

  if file_belongs_to_single_package "$file"; then
    "$@"
  fi
}

if [ "$1" -gt 1 ]; then
  :; 
fi

%preun
package_type=rpm
file_belongs_to_single_package ()
{
  if [ ! -e "$1" ]; then
    false
  elif [ "$package_type" = rpm ]; then
    test `rpm -q --whatprovides "$1" | wc -l` = 1
  elif [ "$package_type" = deb ]; then
    test `dpkg -S "$1" | wc -l` = 1
  else
    exit 1
  fi
}


do_if_file_belongs_to_single_package ()
{
  local file="$1"
  shift

  if file_belongs_to_single_package "$file"; then
    "$@"
  fi
}
#
# Remove $1 desktop file from the list of default handlers for $2 mime type
# in $3 file dumping output to stdout.
#
desktop_filter_out_default_mime_handler ()
{
  local defaults_list="$3"

  local desktop_file="$1"
  local mime_type="$2"

  awk -f- "$defaults_list" <<EOF
  BEGIN {
    mime_type="$mime_type"
    mime_type_regexp="~" mime_type "="
    desktop_file="$desktop_file"
  }
  \$0 ~ mime_type {
    \$0 = substr(\$0, length(mime_type) + 2);
    split(\$0, desktop_files, ";")
    remaining_desktop_files
    counter=0
    for (idx in desktop_files) {
      if (desktop_files[idx] != desktop_file) {
        ++counter;
      }
    }
    if (counter) {
      printf mime_type "="
      for (idx in desktop_files) {
        if (desktop_files[idx] != desktop_file) {
          printf desktop_files[idx]
          if (--counter) {
            printf ";"
          }
        }
      }
      printf "\n"
    }
    next
  }

  { print }
EOF
}


#
# Remove $2 desktop file from the list of default handlers for $@ mime types
# in $1 file.
# Result is saved in $1 file.
#
desktop_uninstall_default_mime_handler_0 ()
{
  local defaults_list=$1
  shift
  [ -f "$defaults_list" ] || return 0

  local desktop_file="$1"
  shift

  tmpfile1=$(mktemp)
  tmpfile2=$(mktemp)
  cat "$defaults_list" > "$tmpfile1"

  local v
  local update=
  for mime in "$@"; do
    desktop_filter_out_default_mime_handler "$desktop_file" "$mime" "$tmpfile1" > "$tmpfile2"
    v="$tmpfile2"
    tmpfile2="$tmpfile1"
    tmpfile1="$v"

    if ! diff -q "$tmpfile1" "$tmpfile2" > /dev/null; then
      update=yes
      desktop_trace Remove $desktop_file default handler for $mime mime type from $defaults_list file
    fi
  done

  if [ -n "$update" ]; then
    cat "$tmpfile1" > "$defaults_list"
    desktop_trace "$defaults_list" file updated
  fi

  rm -f "$tmpfile1" "$tmpfile2"
}


#
# Remove $1 desktop file from the list of default handlers for $@ mime types
# in all known system defaults lists.
#
desktop_uninstall_default_mime_handler ()
{
  for f in /usr/share/applications/defaults.list /usr/local/share/applications/defaults.list; do
    desktop_uninstall_default_mime_handler_0 "$f" "$@"
  done
}


desktop_trace ()
{
  echo "$@"
}




%clean
