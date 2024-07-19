# Teambition-Devops

## 相关文档
- [Teambition DevOps 的 Jenkins 插件说明](https://docs.dingtalk.com/i/p/Qrkoz3py2breX6L73RmQy3o1ZKynQmaP)
- [Teambition DevOps 介绍](https://support.teambition.com/help/docs/60c8648868465f00468fb306)
![png](https://img.alicdn.com/imgextra/i1/O1CN01VRjzwz1lVRbBkbqO8_!!6000000004824-2-tps-1200-563.png)

## Introduction

Teambition-Devops Plugin is an open source Jenkins plugin that can connect your builds with Teambition.

## Install

### Jenkins Marketplace

1. Login to your Jenkins server.
2. Navigate to the Plugin Manager.
3. Select the "Available" tab and search for `Teambition-DevOps` as the plugin name then install it.
4. Restart jenkins for the installation to take effect.

## System Config
Set necessary information at Manage Jenkins -> Configure System -> Teambition DevOps application:
1. Teambition DevOps Endpoint: the URL of Teambition DevOps REST API. The default URL is: https://guiyi.teambition.com.
2. Teambition Organization ID: configured in Teambition DevOps application.
3. Jenkins Config Url: configured in Teambition DevOps application, default is the same as Jenkins URL in Jenkins Location.

## LICENSE

Licensed under GPL 3.0, see [LICENSE](https://www.gnu.org/licenses/gpl-3.0.md)

